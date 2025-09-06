package com._Blog.Backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com._Blog.Backend.model.Comment;
import com._Blog.Backend.services.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/comments")
public class CommentController {
    
    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> createComment(@Valid @RequestBody Comment comment) {
        Comment savedPost = this.commentService.addComment(comment);
        return ResponseEntity.status(HttpStatus.OK).body(savedPost);
    }

    @GetMapping
    public List<Comment> getComments(@RequestParam(defaultValue = "0") Long page) {
        return commentService.getComments(page);
    }
}
