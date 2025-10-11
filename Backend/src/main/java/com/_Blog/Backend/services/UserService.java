package com._Blog.Backend.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com._Blog.Backend.dto.ReportRequest;
import com._Blog.Backend.dto.UserAccountUpdateRequest;
import com._Blog.Backend.dto.UserProfileUpdateRequest;
import com._Blog.Backend.dto.UserResponse;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.exception.UnauthorizedException;
import com._Blog.Backend.model.JwtUser;
import com._Blog.Backend.model.ReportUser;
import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.ReportUserRepository;
import com._Blog.Backend.repository.UserRepository;
import com._Blog.Backend.utils.JwtUtil;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ReportUserRepository reportUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil, ReportUserRepository reportUserRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.reportUserRepository = reportUserRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
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

    public void reportUser(ReportRequest reportRequest) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User reporter = this.userRepository.findById(jwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User reported = this.userRepository.findById(reportRequest.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ReportUser reportUser = new ReportUser(reporter, reported, reportRequest.getDescription());
        this.reportUserRepository.save(reportUser);
    }

    public UserResponse updateProfile(UserProfileUpdateRequest profileUpdateRequest) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userRepository.findById(jwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (profileUpdateRequest.getIconProfile() != null) {
            user.setIconPath(profileUpdateRequest.getIconProfile());
        }
        if (profileUpdateRequest.getUsername() != null) {
            user.setUsername(profileUpdateRequest.getUsername());
        }
        if (profileUpdateRequest.getBio() != null) {
            user.setBio(profileUpdateRequest.getBio());
        }

        User savedUser = this.userRepository.save(user);
        return new UserResponse(savedUser);
    }

    public UserResponse updateAccount(UserAccountUpdateRequest accountUpdateRequest) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userRepository.findById(jwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (accountUpdateRequest.getOldPassword() == null ||
                !passwordEncoder.matches(accountUpdateRequest.getOldPassword(), user.getPassword())) {
            throw new UnauthorizedException("Wrong password");
        }
        if (accountUpdateRequest.getEmail() == null || !user.getEmail().equals(accountUpdateRequest.getEmail())) {
            throw new UnauthorizedException("Wrong email");
        }
        if (accountUpdateRequest.getNewPassword() != null) {
            user.setPassword(passwordEncoder.encode(accountUpdateRequest.getNewPassword()));
        }
        User savedUser = this.userRepository.save(user);
        return new UserResponse(savedUser);
    }
}
