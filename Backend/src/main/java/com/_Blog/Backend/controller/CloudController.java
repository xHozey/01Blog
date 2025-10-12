package com._Blog.Backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com._Blog.Backend.exception.BadRequestException;
import com._Blog.Backend.services.CloudinaryService;

@RestController
@RequestMapping("/api/v1/cloud")
public class CloudController {

    private final CloudinaryService cloudinaryService;

    public CloudController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    private final List<String> ALLOWED_TYPES_IMAGES = List.of("jpg", "jpeg", "png", "gif", "bmp", "tiff", "webp", "svg");
    private final List<String> ALLOWED_TYPES_VIDEOS = List.of("mp4", "avi", "mov", "wmv", "flv", "webm", "mkv", "m4v");

    @PostMapping("/post/image")
    public ResponseEntity<String> savePostImage(@RequestPart(value = "file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && !ALLOWED_TYPES_IMAGES.contains(fileName.substring(fileName.indexOf(".") +1))) {
            throw new BadRequestException("Invalid file type");
        }
        String url = this.cloudinaryService.uploadFile(file, "/post/image");
        return ResponseEntity.ok(url);
    }

    @PostMapping("/post/video")
    public ResponseEntity<String> savePostVideo(@RequestPart(value = "file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && !ALLOWED_TYPES_VIDEOS.contains(fileName.substring(fileName.indexOf(".")+1))) {
            throw new BadRequestException("Invalid file type");
        }
        String url = this.cloudinaryService.uploadFile(file, "/post/video");
        return ResponseEntity.ok(url);
    }

    @PostMapping("/user/icon")
    public ResponseEntity<String> saveIconImage(@RequestPart(value = "file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && !ALLOWED_TYPES_IMAGES.contains(fileName.substring(fileName.indexOf(".")+1))) {
            throw new BadRequestException("Invalid file type");
        }
        String url = this.cloudinaryService.uploadFile(file, "/user");
        return ResponseEntity.ok(url);
    }
}
