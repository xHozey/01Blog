package com._Blog.Backend.controller;

import com._Blog.Backend.dto.LoginRequest;
import com._Blog.Backend.dto.RegisterRequest;
import com._Blog.Backend.dto.ReportRequest;
import com._Blog.Backend.dto.UserResponse;
import com._Blog.Backend.model.JwtUser;
import com._Blog.Backend.utils.CookiesUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/user/{id}/report")
    public ResponseEntity<String> reportUser(@RequestBody ReportRequest reportRequest, @PathVariable Long id) {
        this.userService.reportUser(reportRequest, id);
        return ResponseEntity.ok("report submitted");
    }

}
