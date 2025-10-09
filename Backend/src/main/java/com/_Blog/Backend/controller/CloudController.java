package com._Blog.Backend.controller;

import com._Blog.Backend.services.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/cloud")
public class CloudController {
    private final CloudinaryService cloudinaryService;

    public CloudController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/post/image")
    public ResponseEntity<String> savePostImage(@RequestPart(value = "file") MultipartFile file) {
        String url = this.cloudinaryService.uploadFile(file, "/post/image");
        return ResponseEntity.ok(url);
    }

    @PostMapping("/post/video")
    public ResponseEntity<String> savePostVideo(@RequestPart(value = "file") MultipartFile file) {
        String url = this.cloudinaryService.uploadFile(file, "/post/video");
        return ResponseEntity.ok(url);
    }

    @PostMapping("/user/icon")
    public ResponseEntity<String> saveIconImage(@RequestPart(value = "file") MultipartFile file) {
        String url = this.cloudinaryService.uploadFile(file, "/user");
        return ResponseEntity.ok(url);
    }
    
}
