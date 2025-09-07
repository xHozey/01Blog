package com._Blog.Backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._Blog.Backend.model.Comment;
import com._Blog.Backend.repository.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment addComment(Comment comment) {
        return this.commentRepository.save(comment);
    }

    public List<Comment> getComments(Long offset) {
        return commentRepository.findCommentsByOffsetLimit(offset * 10);
    }
}
