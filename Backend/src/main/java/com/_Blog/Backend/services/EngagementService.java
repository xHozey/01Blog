package com._Blog.Backend.services;

import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.model.*;
import com._Blog.Backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EngagementService {
    private final CommentEngagementRepository commentEngagementRepository;
    private final PostEngagementRepository postEngagementRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public EngagementService(CommentEngagementRepository commentEngagementRepository, PostEngagementRepository postEngagementRepository, PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.commentEngagementRepository = commentEngagementRepository;
        this.postEngagementRepository = postEngagementRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public void addPostLike(Long postId) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<PostEngagement> engagement = postEngagementRepository.findByPostIdAndUserId(postId, jwtUser.getId());
        if (engagement.isPresent()) {
            postEngagementRepository.deleteById(engagement.get().getId());
        } else {
            PostEngagement postEngagement = new PostEngagement();
            User user = userRepository.findById(jwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            Post post =  postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
            postEngagement.setPost(post);
            postEngagement.setUser(user);
            postEngagementRepository.save(postEngagement);
        }
    }

    public void addCommentLike(Long commentId) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<CommentEngagement> engagement = commentEngagementRepository.findByCommentIdAndUserId(commentId, jwtUser.getId());
        if (engagement.isPresent()) {
            commentEngagementRepository.deleteById(engagement.get().getId());
        } else {
            CommentEngagement commentEngagement = new CommentEngagement();
            User user = userRepository.findById(jwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
            commentEngagement.setComment(comment);
            commentEngagement.setUser(user);
            commentEngagementRepository.save(commentEngagement);
        }
    }
}
