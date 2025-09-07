package com._Blog.Backend.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com._Blog.Backend.exception.ConflictException;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.exception.UnauthorizedException;
import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.UserRepository;
import com._Blog.Backend.utils.JwtUtil;
import com._Blog.Backend.exception.BadRequestException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("email already taken");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ConflictException("username already taken");
        }
        user.setPassword(this.encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(User user) {
        if ((user.getEmail() == null || user.getEmail().isBlank())
                && (user.getUsername() == null || user.getUsername().isBlank())) {
            throw new BadRequestException("Email or username must be provided");
        }

        if (user.getUsername() != null && user.getUsername().length() > 64) {
            throw new BadRequestException("Username too long");
        }

        if (user.getEmail() != null && user.getEmail().length() > 255) {
            throw new BadRequestException("Email too long");
        }

        User existingUser = userRepository.findByEmail(user.getEmail())
                .orElseGet(() -> userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));

        if (!encoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(existingUser);
        existingUser.setToken(token);
        userRepository.save(existingUser);
        return token;
    }

}
