package com._Blog.Backend.services;

import com._Blog.Backend.dto.UserRequest;
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
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final UserRoleService userRoleService;
    private final SessionRepository sessionRepository;

    public UserService(UserRepository userRepository, PasswordEncoder encoder, JwtUtil jwtUtil, UserRoleService userRoleService, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.userRoleService = userRoleService;
        this.sessionRepository = sessionRepository;
    }

    public void register(UserRequest user) {
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
        User savedUser = userRepository.save(userEntity);
        userRoleService.addRole(Role.USER, savedUser);
    }

    public String[] login(UserRequest user) {
        User existingUser = userRepository.findByEmail(user.getUsername())
                .orElseGet(() -> userRepository.findByUsername(user.getUsername())
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

    public UserResponse GetUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserResponse(user.getUsername(), user.getIconPath(), user.getCreation());
    }

}
