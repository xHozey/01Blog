package com._Blog.Backend.controller;

import java.io.IOException;
import java.util.List;

import com._Blog.Backend.dto.PostRequest;
import com._Blog.Backend.dto.PostResponse;
import com._Blog.Backend.dto.ReportRequest;
import com._Blog.Backend.exception.BadRequestException;
import com._Blog.Backend.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com._Blog.Backend.services.PostService;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> createPost(@RequestBody @Valid PostRequest postRequest) {
        PostResponse response = postService.addPost(postRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getPosts(@RequestParam(defaultValue = "0") Long page) {
        return ResponseEntity.ok(postService.getPosts(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@RequestBody PostRequest postRequest, @PathVariable Long id) {
        PostResponse savedPost = postService.updatePost(postRequest, id);
        return ResponseEntity.status(HttpStatus.OK).body(savedPost);
    }

    @PostMapping("/report")
    public ResponseEntity<String> reportPost(@Valid @RequestBody ReportRequest reportRequest) {
        this.postService.reportPost(reportRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Post reported successfully");
    }

    @PostMapping("/hide/{id}")
    public ResponseEntity<String> hidePost(@PathVariable Long id) {
        this.postService.hidePost(id);
        return ResponseEntity.status(HttpStatus.OK).body("Post hide successfully");
    }

}
