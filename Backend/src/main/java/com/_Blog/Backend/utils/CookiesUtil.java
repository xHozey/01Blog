package com._Blog.Backend.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public class CookiesUtil {
    public static String AuthToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("auth_token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void SetAuthToken(HttpServletResponse response, String authToken) {
        ResponseCookie cookie = ResponseCookie.from("auth_token", authToken)
            .httpOnly(true)
            .secure(true)        // true in production
            .path("/")
            .sameSite("None")     // critical for cross-origin
            .maxAge( 15)       // 5 minutes
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }


    public static void SetRefreshToken(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)       // true in production
                .path("/")
                .sameSite("None")    // critical for cross-origin requests
                .maxAge(24 * 60 * 60) // 1 day
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }


}
