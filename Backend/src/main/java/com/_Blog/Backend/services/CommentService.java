package com._Blog.Backend.services;

import java.util.List;

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

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment addComment(Comment comment) {
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        comment.setUserId(user.getId());
        return this.commentRepository.save(comment);
    }

    public List<Comment> getComments(Long offset) {
        return commentRepository.findCommentsByOffsetLimit(offset * 10);
    }

    public Comment updateComment(Comment comment) {
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (comment.getId() == null) throw new BadRequestException("Comment id is null");
        Comment oldComment = commentRepository.findById(comment.getId()).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        if (!user.getId().equals(oldComment.getUserId())) throw new UnauthorizedException("You are not allowed to update this comment");

        oldComment.setContent(comment.getContent());
        oldComment.setImagePath(comment.getImagePath());
        oldComment.setVideoPath(comment.getVideoPath());
        return commentRepository.save(oldComment);
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
        if (!comment.getUserId().equals(currentUserId) && !isAdmin) {
            throw new UnauthorizedException("You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);
    }
}
