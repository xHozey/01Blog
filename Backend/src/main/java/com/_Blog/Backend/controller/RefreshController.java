package com._Blog.Backend.controller;

import com._Blog.Backend.services.RefreshService;
import com._Blog.Backend.utils.CookiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/refresh")
public class RefreshController {
    private final RefreshService refreshService;

    @Autowired
    public RefreshController(RefreshService refreshService) {
        this.refreshService = refreshService;
    }

    @PostMapping
    public ResponseEntity<String> RefreshToken(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("refresh token is null");
        }



    }
}
