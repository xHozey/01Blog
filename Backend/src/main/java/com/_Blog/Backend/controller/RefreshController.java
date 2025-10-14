package com._Blog.Backend.controller;

import com._Blog.Backend.services.RefreshService;
import com._Blog.Backend.utils.CookiesUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/refresh")
public class RefreshController {
    private final RefreshService refreshService;

    @Autowired
    public RefreshController(RefreshService refreshService) {
        this.refreshService = refreshService;
    }

    @GetMapping
    public ResponseEntity<String> RefreshToken(HttpServletResponse response,
            @CookieValue(name = "refresh_token", required = false) String refreshToken) {
        try {

            if (refreshToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("refresh token is null");
            }

            String[] tokens = refreshService.refreshToken(refreshToken);

            CookiesUtil.SetRefreshToken(response, tokens[1]);
            CookiesUtil.SetAuthToken(response, tokens[0]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ResponseEntity.ok("Refreshed successfully");
    }
}
