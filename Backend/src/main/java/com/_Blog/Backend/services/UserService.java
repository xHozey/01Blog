package com._Blog.Backend.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com._Blog.Backend.exception.ConflictException;
import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
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
}