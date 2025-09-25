package com._Blog.Backend.controller;

import com._Blog.Backend.model.Follow;
import com._Blog.Backend.services.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<String> follow(@PathVariable Long id) {
        followService.addFollow(id);
        return  ResponseEntity.ok("follow");
    }

    @PostMapping("/{id}/unfollow")
    public ResponseEntity<String> unfollow(@PathVariable Long id) {
        followService.removeFollow(id);
        return  ResponseEntity.ok("unfollow");
    }

    @GetMapping("/followers")
    public ResponseEntity<Long> getFollowers() {
        Long total = followService.getFollowersTotal();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/followed")
    public ResponseEntity<Long> getFollowed() {
        Long total = followService.getFollowedTotal();
        return ResponseEntity.ok(total);
    }
}
