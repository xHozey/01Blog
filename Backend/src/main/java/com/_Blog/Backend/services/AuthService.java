package com._Blog.Backend.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com._Blog.Backend.dto.LoginRequest;
import com._Blog.Backend.dto.RegisterRequest;
import com._Blog.Backend.exception.ConflictException;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.exception.UnauthorizedException;
import com._Blog.Backend.model.Session;
import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.SessionRepository;
import com._Blog.Backend.repository.UserRepository;
import com._Blog.Backend.utils.JwtUtil;
import com._Blog.Backend.utils.Role;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserRoleService userRoleService;
    private final SessionRepository sessionRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder encoder, UserRoleService userRoleService, SessionRepository sessionRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userRoleService = userRoleService;
        this.sessionRepository = sessionRepository;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("email already taken");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ConflictException("username already taken");
        }
        User userEntity = new User();
        userEntity.setEmail(user.getEmail());
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(this.encoder.encode(user.getPassword()));
        userEntity.setIconPath(user.getIconUrl());
        User savedUser = userRepository.save(userEntity);
        userRoleService.addRole(Role.USER, savedUser);
    }

    public String[] login(LoginRequest user) {
        User existingUser = userRepository.findByUsername(user.getUsername())
                .orElseGet(() -> userRepository.findByEmail(user.getUsername())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")));

        if (!encoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        String authToken = jwtUtil.generateAuthToken(existingUser);

        Session session = new Session();
        String refreshToken = jwtUtil.generateRefreshToken(existingUser);
        session.setToken(refreshToken);
        session.setUser(existingUser);
        sessionRepository.save(session);

        return new String[]{authToken, refreshToken};
    }
}
