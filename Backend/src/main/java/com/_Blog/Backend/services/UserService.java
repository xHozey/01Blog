package com._Blog.Backend.services;

import com._Blog.Backend.dto.LoginRequest;
import com._Blog.Backend.dto.RegisterRequest;
import com._Blog.Backend.dto.ReportRequest;
import com._Blog.Backend.dto.UserResponse;
import com._Blog.Backend.model.*;
import com._Blog.Backend.repository.ReportUserRepository;
import com._Blog.Backend.repository.SessionRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com._Blog.Backend.exception.ConflictException;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.exception.UnauthorizedException;
import com._Blog.Backend.repository.UserRepository;
import com._Blog.Backend.utils.JwtUtil;
import com._Blog.Backend.utils.Role;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ReportUserRepository reportUserRepository;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil,  ReportUserRepository reportUserRepository) {
        this.userRepository = userRepository;
        this.reportUserRepository = reportUserRepository;
        this.jwtUtil = jwtUtil;
    }

    public UserResponse GetUser(String token) {
        Long userId = jwtUtil.extractId(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserResponse(user);
    }

    public UserResponse GetUserById(Long userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserResponse(user);
    }

    public void reportUser(ReportRequest reportRequest, Long reportedId) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User reporter = this.userRepository.findById(jwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User reported = this.userRepository.findById(reportedId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ReportUser reportUser = new ReportUser(reporter, reported, reportRequest.getDescription());
        this.reportUserRepository.save(reportUser);
    }
}
