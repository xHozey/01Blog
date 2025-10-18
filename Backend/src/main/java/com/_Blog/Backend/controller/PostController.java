package com._Blog.Backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com._Blog.Backend.dto.PostRequest;
import com._Blog.Backend.dto.PostResponse;
import com._Blog.Backend.dto.ReportRequest;
import com._Blog.Backend.services.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody @Valid PostRequest postRequest) {
        PostResponse response = postService.addPost(postRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getPosts(@RequestParam(defaultValue = "0") Integer page) {
        return ResponseEntity.ok(postService.getPosts(page));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<PostResponse>> getUserPosts(@RequestParam(defaultValue = "0") Integer page,
            @PathVariable Long id) {
        return ResponseEntity.ok(postService.getUserPosts(id, page));
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
    public ResponseEntity<PostResponse> updatePost(@RequestBody @Valid PostRequest postRequest, @PathVariable Long id) {
        PostResponse savedPost = postService.updatePost(postRequest, id);
        return ResponseEntity.status(HttpStatus.OK).body(savedPost);
    }

    @PostMapping("/{id}/report")
    public ResponseEntity<String> reportPost(@RequestBody @Valid ReportRequest reportRequest, @PathVariable Long id) {
        this.postService.reportPost(reportRequest, id);
        return ResponseEntity.ok("Post reported successfully");
    }

}
