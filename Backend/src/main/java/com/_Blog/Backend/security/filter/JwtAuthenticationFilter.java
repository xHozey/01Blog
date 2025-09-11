package com._Blog.Backend.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
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

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {
        final String authenticationToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authenticationToken == null || authenticationToken.trim().isEmpty() || !authenticationToken.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        final String token = authenticationToken.split(" ")[1].trim();
        
        if (jwtUtil.isTokenExpired(token)) {
            chain.doFilter(request, response);
            return;
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
        chain.doFilter(request, response);
    }
}
