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
@RequestMapping("api/v1/comments")
public class CommentController {
    
    private final CommentService commentService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public CommentController(CommentService commentService, CloudinaryService cloudinaryService) {
        this.commentService = commentService;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestPart("postId") String idStr,
                                                         @RequestPart("content") String content,
                                                         @RequestPart(value = "file", required = false) MultipartFile file)
    {
        long id = 0;
        try {
            Long.parseLong(idStr);
        }  catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String fileUrl = file != null ? this.cloudinaryService.uploadFile(file, "/comments") : null;
        CommentRequest comment = new CommentRequest(content, fileUrl, id);
        CommentResponse savedPost = this.commentService.addComment(comment);
        return ResponseEntity.status(HttpStatus.OK).body(savedPost);
    }

    @GetMapping
    public List<CommentResponse> getComments(@RequestParam(defaultValue = "0") Long page) {
        return commentService.getComments(page);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    @PutMapping
    public ResponseEntity<CommentResponse> updateComment(@RequestPart("postId") String postIdStr,
                                                 @RequestPart("commentId") String commentIdStr,
                                                 @RequestPart("content") String content,
                                                 @RequestPart(value = "file", required = false) MultipartFile file) {
        long commentId = 0;
        long postId = 0;
        try {
            commentId = Long.parseLong(commentIdStr);
            postId = Long.parseLong(postIdStr);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String fileUrl = file != null ? this.cloudinaryService.uploadFile(file, "/comments") : null;
        CommentRequest comment = new CommentRequest(content, fileUrl, postId);
        CommentResponse savedComment = commentService.updateComment(comment, commentId);
        return ResponseEntity.status(HttpStatus.OK).body(savedComment);
    }
}
