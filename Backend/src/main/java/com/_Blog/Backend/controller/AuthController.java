package com._Blog.Backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._Blog.Backend.dto.LoginRequest;
import com._Blog.Backend.dto.RegisterRequest;
import com._Blog.Backend.services.AuthService;
import com._Blog.Backend.services.RefreshService;
import com._Blog.Backend.utils.CookiesUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshService refreshService;

    @Autowired
    public AuthController(AuthService authService, RefreshService refreshService) {
        this.authService = authService;
        this.refreshService = refreshService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest user) {
        this.authService.register(user);
        return ResponseEntity.ok("user registered");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest user, HttpServletResponse response) {
        try {
            String[] tokens = authService.login(user);
            CookiesUtil.SetAuthToken(response, tokens[0]);
            CookiesUtil.SetRefreshToken(response, tokens[1]);
        } catch(Exception e) {
            System.out.println("test: "+e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok("Logged in successfully, cookie set!");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response,
            @CookieValue(name = "refresh_token", required = false) String refreshToken) {
        if (refreshToken != null) {
            this.refreshService.revokeSession(refreshToken);
        }
        CookiesUtil.deleteCookies(response);
        return ResponseEntity.ok("logout succesfuly");
    }

    @GetMapping("/check-auth")
    public ResponseEntity<Boolean> checkAuth(@CookieValue(name = "auth_token", required = false) String authToken) {
        if (authToken == null || !this.authService.isAuth(authToken)) {
            return ResponseEntity.status(401).body(false);
        }
        return ResponseEntity.ok(true);
    }

    @GetMapping("/check-unauth")
    public ResponseEntity<Boolean> checkUnauth(
            @CookieValue(name = "auth_token", required = false) String authToken,
            @CookieValue(name = "refresh_token", required = false) String refreshToken) {

        if (refreshToken != null) {
            return ResponseEntity.ok(false);
        }
        if (authToken != null && this.authService.isAuth(authToken)) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);
    }

}
