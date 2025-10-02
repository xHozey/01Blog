package com._Blog.Backend.services;

import java.util.ArrayList;
import java.util.List;

import com._Blog.Backend.dto.PostRequest;
import com._Blog.Backend.dto.PostResponse;
import com._Blog.Backend.dto.ReportRequest;
import com._Blog.Backend.model.*;
import com._Blog.Backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.exception.UnauthorizedException;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PostEngagementRepository postEngagementRepository;
    private final ReportPostRepository reportPostRepository;
    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, FollowRepository followRepository, PostEngagementRepository postEngagementRepository, ReportPostRepository reportPostRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.postEngagementRepository = postEngagementRepository;
        this.reportPostRepository = reportPostRepository;
    }

    public PostResponse addPost(PostRequest post) {
        JwtUser JwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(JwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Post newPost = new Post();
        newPost.setUser(user);
        newPost.setTitle(post.getTitle());
        newPost.setContent(post.getContent());
        newPost.setFilePath(post.getFilePath());
        Post savedPost = this.postRepository.save(newPost);
        return new PostResponse(savedPost.getId(), savedPost.getTitle(), savedPost.getContent(), savedPost.getUser().getUsername(),savedPost.getUser().getId(), savedPost.getFilePath(),savedPost.getCreateTime(), 0L, false );
    }

    public List<PostResponse> getPosts(Long offset) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Long> followedUserIds = followRepository.findByFollowerId(jwtUser.getId())
                .stream()
                .map(follow -> follow.getFollowed().getId())
                .toList();

        List<Post> postsByFollowedUsers = postRepository.findPostsByUserIds(followedUserIds, offset * 10);

        List<PostResponse> resultPosts = new ArrayList<>();
        int remaining = 10;
        if (!followedUserIds.isEmpty()) {

            remaining = getRemaining(jwtUser, postsByFollowedUsers, resultPosts, remaining);
            System.out.println(remaining);

            if (remaining > 0) {
                List<Post> postsFromOthers = postRepository.findRandomPostsExcludingUsers(followedUserIds);
                getRemaining(jwtUser, postsFromOthers, resultPosts, remaining);
                System.out.println(postsFromOthers);
            }
        } else {
            List<Post> posts = this.postRepository.findNewestPosts(offset * 10);
            getRemaining(jwtUser, posts, resultPosts, remaining);
        }
        return resultPosts;
    }

    private int getRemaining(JwtUser jwtUser, List<Post> postsByFollowedUsers, List<PostResponse> resultPosts, int remaining) {
        for (Post post : postsByFollowedUsers) {
            if (remaining == 0) break;
            resultPosts.add(new PostResponse(post.getId(), post.getTitle(),post.getContent(), post.getUser().getUsername(),post.getUser().getId(), post.getFilePath(),post.getCreateTime(),this.postEngagementRepository.countByPostId(post.getId()), this.postEngagementRepository.existsByPostIdAndUserId(post.getId(), jwtUser.getId())));
            remaining--;
        }
        return remaining;
    }


    public PostResponse getPostById(Long id) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Post not found with id %d", id)));
        return new PostResponse(post.getId(), post.getTitle(),post.getContent(), post.getUser().getUsername(),post.getUser().getId(), post.getFilePath(),post.getCreateTime(),this.postEngagementRepository.countByPostId(post.getId()), this.postEngagementRepository.existsByPostIdAndUserId(post.getId(), jwtUser.getId()));
    }

    public PostResponse updatePost(PostRequest post, Long postId) {
        JwtUser JwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user =  userRepository.findById(JwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with id %d", JwtUser.getId())));
        Post oldPost = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        if (!user.getId().equals(oldPost.getUser().getId())) throw new UnauthorizedException("You are not allowed to update this post");

        oldPost.setTitle(post.getTitle());   
        oldPost.setContent(post.getContent());

        if (post.getFilePath() != null) oldPost.setFilePath(post.getFilePath());
        Post savedPost = this.postRepository.save(oldPost);
        return new PostResponse(savedPost.getId(), savedPost.getTitle(), savedPost.getContent(), savedPost.getUser().getUsername(),savedPost.getUser().getId(), savedPost.getFilePath(),savedPost.getCreateTime(), this.postEngagementRepository.countByPostId(savedPost.getId()),this.postEngagementRepository.existsByPostIdAndUserId(postId, JwtUser.getId()) );
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Post not found with id %d", postId)));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JwtUser currentUser = (JwtUser) auth.getPrincipal();
        Long currentUserId = currentUser.getId();
        boolean isAdmin = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
        if (!post.getUser().getId().equals(currentUserId) && !isAdmin) {
            throw new UnauthorizedException("You are not allowed to delete this post");
        }

        postRepository.delete(post);
    }

    public void reportPost(ReportRequest reportRequest) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = this.postRepository.findById(reportRequest.getPostId()).orElseThrow(() -> new ResourceNotFoundException(String.format("Post not found with id %d", reportRequest.getPostId())));
        ReportPost reportPost = new ReportPost();
        reportPost.setReported(post);
        User user = this.userRepository.findById(jwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with id %d", jwtUser.getId())));
        reportPost.setReporter(user);
        reportPost.setDescription(reportRequest.getDescription());
        this.reportPostRepository.save(reportPost);
    }

    public void hidePost(Long postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException(String.format("Post not found with id %d", postId)));
        post.setIsHide(!post.getIsHide());
        this.postRepository.save(post);
    }
}
