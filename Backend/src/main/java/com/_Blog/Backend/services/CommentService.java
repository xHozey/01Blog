package com._Blog.Backend.services;

import java.util.List;

import com._Blog.Backend.model.*;
import com._Blog.Backend.repository.*;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com._Blog.Backend.dto.CommentRequest;
import com._Blog.Backend.dto.CommentResponse;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.exception.UnauthorizedException;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentEngagementRepository commentEngagementRepository;
    private final PostRepository postRepository;

    public CommentResponse addComment(CommentRequest comment) {
        JwtUser JwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(JwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Comment newComment = new Comment();
        newComment.setContent(comment.getContent());
        newComment.setUser(user);
        newComment.setPost(this.postRepository.findById(comment.getPostId()).orElseThrow(() -> new ResourceNotFoundException("Post not found")));
        Comment savedComment = this.commentRepository.save(newComment);
        return new CommentResponse(savedComment, this.commentEngagementRepository.countByCommentId(savedComment.getId()), this.commentEngagementRepository.existsByCommentIdAndUserId(savedComment.getId(), JwtUser.getId()));
    }

    public List<CommentResponse> getComments(Integer page, Long postId) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Comment> comments = commentRepository.findAllByPostId(postId, PageRequest.of(page, 10, Sort.by("createTime").descending())).getContent();

        return comments.stream()
                .map(comment -> new CommentResponse(comment, this.commentEngagementRepository.countByCommentId(comment.getId()), this.commentEngagementRepository.existsByCommentIdAndUserId(comment.getId(), jwtUser.getId())))
                .toList();
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                String.format("comment not found with id %d", commentId)));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JwtUser currentUser = (JwtUser) auth.getPrincipal();
        Long currentUserId = currentUser.getId();

        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedException("You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);
    }

}
