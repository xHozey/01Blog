package com._Blog.Backend.controller;

import java.util.List;

import com._Blog.Backend.dto.CommentRequest;
import com._Blog.Backend.dto.CommentResponse;
import com._Blog.Backend.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com._Blog.Backend.model.Comment;
import com._Blog.Backend.services.CommentService;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest) {
        CommentResponse savedPost = this.commentService.addComment(commentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(savedPost);
    }

    @GetMapping("{id}")
    public ResponseEntity<List<CommentResponse>> getComments(@RequestParam(defaultValue = "0") Integer page, @PathVariable Long id) {
        return ResponseEntity.ok(commentService.getComments(page, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    @PutMapping("{id}")
    public ResponseEntity<CommentResponse> updateComment(@RequestBody CommentRequest commentRequest, @PathVariable Long id) {
        CommentResponse savedComment = commentService.updateComment(commentRequest, id);
        return ResponseEntity.status(HttpStatus.OK).body(savedComment);
    }
}
