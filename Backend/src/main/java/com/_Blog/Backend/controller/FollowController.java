package com._Blog.Backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._Blog.Backend.services.FollowService;

@RestController
@RequestMapping("/api/v1/users")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<String> follow(@PathVariable Long id) {
        followService.toggleFollow(id);
        return ResponseEntity.ok("follow");
    }

    @GetMapping("/{id}/follow")
    public ResponseEntity<Boolean> checkIsFollow(@PathVariable Long id) {
        return ResponseEntity.ok(followService.isFollowing(id));
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<Long> getFollowers(@PathVariable Long id) {
        Long total = followService.getFollowersTotal(id);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/{id}/followed")
    public ResponseEntity<Long> getFollowed(@PathVariable Long id) {
        Long total = followService.getFollowedTotal(id);
        return ResponseEntity.ok(total);
    }
}
