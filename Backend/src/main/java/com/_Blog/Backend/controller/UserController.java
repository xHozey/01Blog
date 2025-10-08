package com._Blog.Backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._Blog.Backend.dto.ReportRequest;
import com._Blog.Backend.dto.UserProfileUpdateRequest;
import com._Blog.Backend.dto.UserResponse;
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
    public ResponseEntity<UserResponse> getCurrentUser(@CookieValue(name = "auth_token", required = false) String token) {

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userService.GetUser(token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        this.userService.GetUserById(id);
        return ResponseEntity.ok(userService.GetUserById(id));
    }

    @PostMapping("/{id}/report")
    public ResponseEntity<String> reportUser(@RequestBody ReportRequest reportRequest, @PathVariable Long id) {
        this.userService.reportUser(reportRequest, id);
        return ResponseEntity.ok("report submitted");
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(@RequestBody @Valid UserProfileUpdateRequest userProfileUpdateRequest) {
        UserResponse user = this.userService.updateProfile(userProfileUpdateRequest);
        return  ResponseEntity.ok(user);
    }
}
