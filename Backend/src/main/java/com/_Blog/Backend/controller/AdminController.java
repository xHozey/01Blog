package com._Blog.Backend.controller;

import com._Blog.Backend.dto.PostResponse;
import com._Blog.Backend.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @PostMapping("/post/{id}/hide")
    public void hidePost(@PathVariable Long id){}

    @DeleteMapping("/post/{id}/delete")
    public void deletePost(@PathVariable Long id){}

    @PostMapping("/comment/{id}/hide")
    public void hideComment(@PathVariable Long id){}

    @DeleteMapping("/comment/{id}/delete")
    public void deleteComment(@PathVariable Long id){}

    @GetMapping("/post")
    public ResponseEntity<List<PostResponse>> getPosts(@RequestParam(defaultValue = "0") Integer page){

    }

    @GetMapping("/user")
    public ResponseEntity<List<UserResponse>> getUsers(@RequestParam(defaultValue = "0") Integer page) {

    }

}
