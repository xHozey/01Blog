package com._Blog.Backend.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com._Blog.Backend.dto.PostRequest;
import com._Blog.Backend.dto.PostResponse;
import com._Blog.Backend.dto.ReportRequest;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.exception.UnauthorizedException;
import com._Blog.Backend.model.JwtUser;
import com._Blog.Backend.model.Post;
import com._Blog.Backend.model.ReportPost;
import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.FollowRepository;
import com._Blog.Backend.repository.PostEngagementRepository;
import com._Blog.Backend.repository.PostRepository;
import com._Blog.Backend.repository.ReportPostRepository;
import com._Blog.Backend.repository.UserRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PostEngagementRepository postEngagementRepository;
    private final ReportPostRepository reportPostRepository;
    private final NotificationService notificationService;

    public PostService(PostRepository postRepository, UserRepository userRepository, FollowRepository followRepository, PostEngagementRepository postEngagementRepository, ReportPostRepository reportPostRepository,  NotificationService notificationService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.postEngagementRepository = postEngagementRepository;
        this.reportPostRepository = reportPostRepository;
        this.notificationService = notificationService;
    }

    public PostResponse addPost(PostRequest post) {
        JwtUser JwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(JwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Post newPost = new Post(post.getTitle(), post.getContent(), user);

        Post savedPost = this.postRepository.save(newPost);
        this.notificationService.notifyFollowers(user, String.format("%s has posted new blog", user.getUsername()));
        return new PostResponse(savedPost, this.postEngagementRepository.countByPostId(savedPost.getId()), this.postEngagementRepository.existsByPostIdAndUserId(savedPost.getId(), JwtUser.getId()));
    }

    public List<PostResponse> getPosts(Integer page) {
        try {
            
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Long> followedUserIds = followRepository.findByFollowerId(jwtUser.getId())
                .stream()
                .map(follow -> follow.getFollowed().getId())
                .toList();

        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<PostResponse> resultPosts = new ArrayList<>();
        int remaining = 10;

        if (!followedUserIds.isEmpty()) {
            List<Post> postsByFollowedUsers = postRepository
                    .findAllByUserIdInAndIsHideFalse(followedUserIds, pageable)
                    .getContent();

            remaining = getRemaining(jwtUser, postsByFollowedUsers, resultPosts, remaining);

            if (remaining > 0) {
                List<Post> postsFromOthers = postRepository
                        .findRandomPostsExcludingUsers(followedUserIds, PageRequest.of(0, remaining))
                        .getContent();

                getRemaining(jwtUser, postsFromOthers, resultPosts, remaining);
            }
        } else {
            List<Post> newestPosts = postRepository
                    .findAllByIsHideFalse(pageable)
                    .getContent();

            getRemaining(jwtUser, newestPosts, resultPosts, remaining);
        }

        return resultPosts;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private int getRemaining(JwtUser jwtUser, List<Post> posts, List<PostResponse> resultPosts, int remaining) {
        for (Post post : posts) {
            if (remaining == 0) break;
            resultPosts.add(new PostResponse(
                    post,
                    postEngagementRepository.countByPostId(post.getId()),
                    postEngagementRepository.existsByPostIdAndUserId(post.getId(), jwtUser.getId())
            ));
            remaining--;
        }
        return remaining;
    }

    public PostResponse getPostById(Long id) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Post not found with id %d", id)));
        return new PostResponse(post, this.postEngagementRepository.countByPostId(post.getId()), this.postEngagementRepository.existsByPostIdAndUserId(post.getId(), jwtUser.getId()));
    }

    public PostResponse updatePost(PostRequest post, Long postId) {
        JwtUser JwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user =  userRepository.findById(JwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with id %d", JwtUser.getId())));
        Post oldPost = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        if (!user.getId().equals(oldPost.getUser().getId())) throw new UnauthorizedException("You are not allowed to update this post");
    
        oldPost.setTitle(post.getTitle());   
        oldPost.setContent(post.getContent());

        Post savedPost = this.postRepository.save(oldPost);
        return new PostResponse(savedPost, this.postEngagementRepository.countByPostId(savedPost.getId()), this.postEngagementRepository.existsByPostIdAndUserId(savedPost.getId(), user.getId()));
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Post not found with id %d", postId)));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JwtUser currentUser = (JwtUser) auth.getPrincipal();
        Long currentUserId = currentUser.getId();

        if (!post.getUser().getId().equals(currentUserId)) {
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

}
