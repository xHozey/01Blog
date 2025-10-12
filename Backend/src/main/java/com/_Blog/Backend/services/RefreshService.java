package com._Blog.Backend.services;

import org.springframework.stereotype.Service;

import com._Blog.Backend.exception.UnauthorizedException;
import com._Blog.Backend.model.Session;
import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.SessionRepository;
import com._Blog.Backend.repository.UserRepository;
import com._Blog.Backend.utils.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Service
public class RefreshService {

    private final JwtUtil jwtUtil;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public RefreshService(JwtUtil jwtUtil, SessionRepository sessionRepository, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public void revokeSession(String refreshToken) {
        sessionRepository.findByToken(refreshToken)
                .ifPresent(session -> {
                    session.setRevoked(true);
                    sessionRepository.save(session);
                });
    }

    public String[] refreshToken(String refreshToken) {
        try {
            if (jwtUtil.isTokenExpired(refreshToken)) {
                throw new UnauthorizedException("refresh token expired");
            }

            User user = userRepository.findById(jwtUtil.extractId(refreshToken))
                    .orElseThrow(() -> {
                        return new UnauthorizedException("user not found");
                    });

            if (user.getIsBanned()) {
                throw new UnauthorizedException("You are banned from the platform");
            }

            Session session = sessionRepository.findByToken(refreshToken)
                    .orElseThrow(() -> {
                        System.out.println("session not found");
                        return new UnauthorizedException("session not found");
                    });

            if (session.getRevoked()) {
                throw new UnauthorizedException("session already revoked");
            }

            session.setRevoked(true);
            sessionRepository.save(session);

            Session newSession = new Session();
            newSession.setUser(user);

            String newRefreshToken = jwtUtil.generateRefreshToken(user);
            String newAuthToken = jwtUtil.generateAuthToken(user);

            newSession.setToken(newRefreshToken);
            sessionRepository.save(newSession);

            return new String[] { newAuthToken, newRefreshToken };

        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("refresh token expired");
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException("invalid refresh token");
        }
    }

}
