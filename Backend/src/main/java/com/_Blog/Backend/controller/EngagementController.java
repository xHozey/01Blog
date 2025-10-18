package com._Blog.Backend.controller;

import com._Blog.Backend.services.EngagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/engagement")
public class EngagementController {
    private final EngagementService engagementService;

    public EngagementController(EngagementService engagementService) {
        this.engagementService = engagementService;
    }

    @PostMapping("/{id}/post/like")
    public ResponseEntity<String> postLike(@PathVariable Long id) {
        engagementService.addPostLike(id);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/{id}/comment/like")
    public ResponseEntity<String> commentLike(@PathVariable Long id) {
        engagementService.addCommentLike(id);
        return ResponseEntity.ok("success");
    }

}
