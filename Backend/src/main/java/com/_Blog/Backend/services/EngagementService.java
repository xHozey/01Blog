package com._Blog.Backend.services;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.model.Comment;
import com._Blog.Backend.model.CommentEngagement;
import com._Blog.Backend.model.JwtUser;
import com._Blog.Backend.model.Post;
import com._Blog.Backend.model.PostEngagement;
import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.CommentEngagementRepository;
import com._Blog.Backend.repository.CommentRepository;
import com._Blog.Backend.repository.PostEngagementRepository;
import com._Blog.Backend.repository.PostRepository;
import com._Blog.Backend.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EngagementService {

    private final CommentEngagementRepository commentEngagementRepository;
    private final PostEngagementRepository postEngagementRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public void addPostLike(Long postId) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<PostEngagement> engagement = postEngagementRepository.findByPostIdAndUserId(postId, jwtUser.getId());
        if (engagement.isPresent()) {
            postEngagementRepository.deleteById(engagement.get().getId());
        } else {
            User user = userRepository.findById(jwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
            postEngagementRepository.save(new PostEngagement(post, user));
        }
    }

    public void addCommentLike(Long commentId) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<CommentEngagement> engagement = commentEngagementRepository.findByCommentIdAndUserId(commentId, jwtUser.getId());
        if (engagement.isPresent()) {
            commentEngagementRepository.deleteById(engagement.get().getId());
        } else {
            User user = userRepository.findById(jwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
            commentEngagementRepository.save(new CommentEngagement(user, comment));
        }
    }
}
