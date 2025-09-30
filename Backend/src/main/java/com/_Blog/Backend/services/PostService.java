package com._Blog.Backend.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com._Blog.Backend.dto.PostResponse;
import com._Blog.Backend.model.*;
import com._Blog.Backend.repository.FollowRepository;
import com._Blog.Backend.repository.PostEngagementRepository;
import com._Blog.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com._Blog.Backend.exception.BadRequestException;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.exception.UnauthorizedException;
import com._Blog.Backend.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PostEngagementRepository postEngagementRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, FollowRepository followRepository, PostEngagementRepository postEngagementRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.postEngagementRepository = postEngagementRepository;
    }

    public Post addPost(Post post) {
        JwtUser JwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(JwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        post.setUser(user);
        return this.postRepository.save(post);
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

        remaining = getRemaining(jwtUser, postsByFollowedUsers, resultPosts, remaining);

        if (remaining > 0) {
            List<Post> postsFromOthers = postRepository.findRandomPostsExcludingUsers(followedUserIds);
            getRemaining(jwtUser, postsFromOthers, resultPosts, remaining);
        }

        return resultPosts;
    }

    private int getRemaining(JwtUser jwtUser, List<Post> postsByFollowedUsers, List<PostResponse> resultPosts, int remaining) {
        for (Post post : postsByFollowedUsers) {
            if (remaining == 0) break;
            resultPosts.add(new PostResponse(post.getId(), post.getTitle(),post.getContent(), post.getUser().getUsername(), post.getVideoPath(),post.getImagePath(),post.getCreateTime(),this.postEngagementRepository.countByPostId(post.getId()), this.postEngagementRepository.existsByPostIdAndUserId(post.getId(), jwtUser.getId())));
            remaining--;
        }
        return remaining;
    }


    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Post not found with id %d", id)));
    }

    public Post updatePost(Post post) {
        JwtUser JwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user =  userRepository.findById(JwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with id %d", JwtUser.getId())));
        if (post.getId() == null) throw new BadRequestException("Post id is null");
        Post oldPost = postRepository.findById(post.getId()).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        if (!user.getId().equals(oldPost.getUser().getId())) throw new UnauthorizedException("You are not allowed to update this post");

        oldPost.setTitle(post.getTitle());   
        oldPost.setContent(post.getContent());
        oldPost.setImagePath(post.getImagePath());
        oldPost.setVideoPath(post.getVideoPath());
        return postRepository.save(oldPost);
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
}
