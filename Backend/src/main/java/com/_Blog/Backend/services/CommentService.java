package com._Blog.Backend.services;

import java.util.List;

import com._Blog.Backend.dto.ReportRequest;
import com._Blog.Backend.model.*;
import com._Blog.Backend.repository.*;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentEngagementRepository commentEngagementRepository;
    private final PostRepository postRepository;
    private final ReportCommentRepository reportCommentRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, CommentEngagementRepository commentEngagementRepository, PostRepository postRepository, ReportCommentRepository reportCommentRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.commentEngagementRepository = commentEngagementRepository;
        this.postRepository = postRepository;
        this.reportCommentRepository = reportCommentRepository;
    }

    public CommentResponse addComment(CommentRequest comment) {
        JwtUser JwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(JwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Comment newComment = new Comment();
        newComment.setContent(comment.getContent());
        newComment.setUser(user);
        newComment.setFilePath(comment.getFilePath());
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

    public CommentResponse updateComment(CommentRequest comment, Long commentId) {
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment oldComment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        if (!user.getId().equals(oldComment.getUser().getId())) {
            throw new UnauthorizedException("You are not allowed to update this comment");
        }
        if (comment.getFilePath() != null) {
            oldComment.setFilePath(comment.getFilePath());
        }
        oldComment.setContent(comment.getContent());
        Comment savedComment = commentRepository.save(oldComment);
        return new CommentResponse(savedComment, this.commentEngagementRepository.countByCommentId(savedComment.getId()), this.commentEngagementRepository.existsByCommentIdAndUserId(savedComment.getId(), user.getId()));
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

    public void reportComment(ReportRequest reportRequest) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = this.commentRepository.findById(reportRequest.getId()).orElseThrow(() -> new ResourceNotFoundException(String.format("Post not found with id %d", reportRequest.getId())));
        User user = this.userRepository.findById(jwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with id %d", jwtUser.getId())));
        ReportComment reportComment = new ReportComment(user, comment, reportRequest.getDescription());
        this.reportCommentRepository.save(reportComment);
    }
}
