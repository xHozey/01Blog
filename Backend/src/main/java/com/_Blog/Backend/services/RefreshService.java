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

    public String[] refreshToken(String refreshToken) {
        try {
            if (jwtUtil.isTokenExpired(refreshToken)) {
                System.out.println("token expired or invalid: " + refreshToken);
                throw new UnauthorizedException("refresh token expired");
            }

            User user = userRepository.findById(jwtUtil.extractId(refreshToken))
                    .orElseThrow(() -> {
                        System.out.println("user not found");
                        return new UnauthorizedException("user not found");
                    });

            Session session = sessionRepository.findByToken(refreshToken)
                    .orElseThrow(() -> {
                        System.out.println("session not found");
                        return new UnauthorizedException("session not found");
                    });

            if (session.getRevoked()) {
                System.out.println("its already revoked");
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

            return new String[]{newAuthToken, newRefreshToken};

        } catch (ExpiredJwtException e) {
            System.out.println("1 token expired or invalid: " + refreshToken);
            throw new UnauthorizedException("refresh token expired");
        } catch (UnauthorizedException e) {
            System.out.println("error: " + e.getMessage());
            throw new UnauthorizedException("invalid refresh token");
        }
    }

}
