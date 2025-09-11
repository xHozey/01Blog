package com._Blog.Backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com._Blog.Backend.model.JwtUser;
import com._Blog.Backend.model.Post;
import com._Blog.Backend.services.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(@Valid @RequestBody Post post) {
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setUserId(user.getId());
        Post savedPost = this.postService.addPost(post);
        return ResponseEntity.status(HttpStatus.OK).body(savedPost);
    }

    @GetMapping
    public List<Post> getPosts(@RequestParam(defaultValue = "0") Long page) {
        return postService.getPosts(page);
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted successfully");
    }

    @PutMapping
    public ResponseEntity<Post> updatePost(@Valid @RequestBody Post post) {
        Post savedPost = postService.updatePost(post);
        return ResponseEntity.status(HttpStatus.OK).body(savedPost);
    }

}
