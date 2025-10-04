package com._Blog.Backend.services;

import java.util.ArrayList;
import java.util.List;

import com._Blog.Backend.dto.CommentRequest;
import com._Blog.Backend.dto.CommentResponse;
import com._Blog.Backend.model.CommentEngagement;
import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.CommentEngagementRepository;
import com._Blog.Backend.repository.PostRepository;
import com._Blog.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com._Blog.Backend.exception.BadRequestException;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.exception.UnauthorizedException;
import com._Blog.Backend.model.Comment;
import com._Blog.Backend.model.JwtUser;
import com._Blog.Backend.repository.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentEngagementRepository commentEngagementRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository,  CommentEngagementRepository commentEngagementRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.commentEngagementRepository = commentEngagementRepository;
        this.postRepository = postRepository;
    }

    public CommentResponse addComment(CommentRequest comment) {
        JwtUser JwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(JwtUser.getId()).orElseThrow(()  -> new ResourceNotFoundException("User not found"));
        Comment newComment = new Comment();
        newComment.setContent(comment.getContent());
        newComment.setUser(user);
        newComment.setFilePath(comment.getFilePath());
        newComment.setPost(this.postRepository.findById(comment.getPostId()).orElseThrow(()  -> new ResourceNotFoundException("Post not found")));
        Comment savedComment = this.commentRepository.save(newComment);
        return new CommentResponse(savedComment.getId(), savedComment.getContent(), savedComment.getUser().getUsername(), savedComment.getUser().getId(), savedComment.getCreateTime(), 0L, false, savedComment.getFilePath());
    }

    public List<CommentResponse> getComments(Long offset, Long postId) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Comment> comments = commentRepository.findCommentsByOffsetLimit(offset * 10, postId);

        return comments.stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getUser().getUsername(),
                        comment.getUser().getId(),
                        comment.getCreateTime(),
                        this.commentEngagementRepository.countByCommentId(comment.getId()),
                        this.commentEngagementRepository.existsByCommentIdAndUserId(comment.getId(), jwtUser.getId()),
                        comment.getFilePath()
                ))
                .toList();
    }

    public CommentResponse updateComment(CommentRequest comment, Long commentId) {
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment oldComment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        if (!user.getId().equals(oldComment.getUser().getId())) throw new UnauthorizedException("You are not allowed to update this comment");
        if (comment.getFilePath() != null) oldComment.setFilePath(comment.getFilePath());
        oldComment.setContent(comment.getContent());
        Comment savedComment = commentRepository.save(oldComment);
        return new CommentResponse(savedComment.getId(), savedComment.getContent(), savedComment.getUser().getUsername(), savedComment.getUser().getId(), savedComment.getCreateTime(), this.commentEngagementRepository.countByCommentId(savedComment.getId()), this.commentEngagementRepository.existsByCommentIdAndUserId(savedComment.getId(), user.getId()), savedComment.getFilePath());
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                String.format("comment not found with id %d", commentId)));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JwtUser currentUser = (JwtUser) auth.getPrincipal();
        Long currentUserId = currentUser.getId();
        boolean isAdmin = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
        if (!comment.getUser().getId().equals(currentUserId) && !isAdmin) {
            throw new UnauthorizedException("You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);
    }
}
