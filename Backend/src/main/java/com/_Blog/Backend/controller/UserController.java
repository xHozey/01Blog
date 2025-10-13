package com._Blog.Backend.controller;

import java.util.List;

import com._Blog.Backend.dto.UserAccountUpdateRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com._Blog.Backend.dto.ReportRequest;
import com._Blog.Backend.dto.UserProfileUpdateRequest;
import com._Blog.Backend.dto.UserResponse;
import com._Blog.Backend.dto.UserSuggetion;
import com._Blog.Backend.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.GetUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        this.userService.GetUserById(id);
        return ResponseEntity.ok(userService.GetUserById(id));
    }

    @PostMapping("/report")
    public ResponseEntity<String> reportUser(@RequestBody ReportRequest reportRequest) {
        this.userService.reportUser(reportRequest);
        return ResponseEntity.ok("report submitted");
    }

    @PutMapping("/me/profile")
    public ResponseEntity<UserResponse> updateProfile(
            @RequestBody @Valid UserProfileUpdateRequest userProfileUpdateRequest) {
        UserResponse user = this.userService.updateProfile(userProfileUpdateRequest);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me/account")
    public ResponseEntity<UserResponse> updateAccount(
            @RequestBody @Valid UserAccountUpdateRequest userAccountUpdateRequest) {
        UserResponse user = this.userService.updateAccount(userAccountUpdateRequest);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/suggetion")
    public ResponseEntity<List<UserSuggetion>> getSuggetion(@RequestParam(defaultValue = "0") Integer page) {
        return ResponseEntity.ok(this.userService.getUsersSuggetion(page));
    }
}
