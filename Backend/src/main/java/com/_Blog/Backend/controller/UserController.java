package com._Blog.Backend.controller;

import com._Blog.Backend.dto.LoginRequest;
import com._Blog.Backend.dto.RegisterRequest;
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

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest user) {
        this.userService.register(user);
        return ResponseEntity.ok("user registered");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest user, HttpServletResponse response) {

        String[] tokens = userService.login(user);

        CookiesUtil.SetAuthToken(response, tokens[0]);
        CookiesUtil.SetRefreshToken(response, tokens[1]);

        return ResponseEntity.ok("Logged in successfully, cookie set!");
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@CookieValue(name = "auth_token", required = false) String token) {
        System.out.println(token + " fff");
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userService.GetUser(token));
    }

}
