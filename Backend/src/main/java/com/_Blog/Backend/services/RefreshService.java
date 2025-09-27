package com._Blog.Backend.services;

import com._Blog.Backend.exception.UnauthorizedException;
import com._Blog.Backend.model.Session;
import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.SessionRepository;
import com._Blog.Backend.repository.UserRepository;
import com._Blog.Backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefreshService {
    private final JwtUtil jwtUtil;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Autowired
    public RefreshService(JwtUtil jwtUtil, SessionRepository sessionRepository, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public String refreshToken(String refreshToken) {
        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new UnauthorizedException("refresh token expired");
        }
        User user = userRepository.findById(jwtUtil.extractId(refreshToken)).orElseThrow(() -> new UnauthorizedException("user not found"));
        Session session = sessionRepository.findByToken(refreshToken).orElseThrow(() -> new UnauthorizedException("session not found"));
        if (session.getRevoked()) {
            throw new UnauthorizedException("session already revoked");
        }
        session.setRevoked(true);
        sessionRepository.save(session);
        Session newSession = new Session();
        newSession.setUser(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);
        newSession.setToken(newRefreshToken);
        sessionRepository.save(newSession);
        return newRefreshToken;
    }
}
