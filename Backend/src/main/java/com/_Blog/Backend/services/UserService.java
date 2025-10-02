package com._Blog.Backend.services;

import com._Blog.Backend.dto.LoginRequest;
import com._Blog.Backend.dto.RegisterRequest;
import com._Blog.Backend.dto.UserResponse;
import com._Blog.Backend.model.Session;
import com._Blog.Backend.repository.SessionRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com._Blog.Backend.exception.ConflictException;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.exception.UnauthorizedException;
import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.UserRepository;
import com._Blog.Backend.utils.JwtUtil;
import com._Blog.Backend.utils.Role;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;

        this.jwtUtil = jwtUtil;
    }

    public UserResponse GetUser(String token) {
        Long userId = jwtUtil.extractId(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserResponse(user.getUsername(), user.getIconPath(), user.getCreation());
    }

}
