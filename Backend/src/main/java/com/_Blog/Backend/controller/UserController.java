package com._Blog.Backend.controller;

import com._Blog.Backend.dto.UserRequest;
import com._Blog.Backend.dto.UserResponse;
import com._Blog.Backend.model.JwtUser;
import com._Blog.Backend.utils.CookiesUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com._Blog.Backend.model.User;
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

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRequest user) {
        this.userService.register(user);
        return ResponseEntity.ok("user registered");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserRequest user, HttpServletResponse response) {
        String[] tokens = userService.login(user);

        CookiesUtil.SetAuthToken(response, tokens[0]);
        CookiesUtil.SetRefreshToken(response, tokens[1]);

        return ResponseEntity.ok("Logged in successfully, cookie set!");
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.GetUser(jwtUser.getId()));
    }
}
