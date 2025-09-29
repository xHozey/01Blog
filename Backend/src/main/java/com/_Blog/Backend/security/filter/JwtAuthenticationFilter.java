package com._Blog.Backend.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com._Blog.Backend.utils.CookiesUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com._Blog.Backend.model.JwtUser;
import com._Blog.Backend.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final Set<String> PUBLIC_ENDPOINTS = Set.of(
            "/api/v1/users/register",
            "/api/v1/users/login",
            "/api/v1/refresh",
            "/api/v1/users/me"
    );

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (PUBLIC_ENDPOINTS.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        final String token = CookiesUtil.AuthToken(request);

        if (token == null) {
            System.out.println(path);
            throw new AuthenticationServiceException("No auth_token provided");
        }

        try {
            if (jwtUtil.isTokenExpired(token)) {
                throw new AuthenticationServiceException("Token is expired");
            }

            String username = jwtUtil.extractUsername(token);
            List<SimpleGrantedAuthority> authorities = jwtUtil.extractRoles(token);
            Long userId = jwtUtil.extractId(token);
            JwtUser user = new JwtUser(userId, username);

        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        authorities
                );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch(Exception e) {
            throw new AuthenticationServiceException("Invalid token: " + e.getMessage());
        }
        chain.doFilter(request, response);
    }
}
