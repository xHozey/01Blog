package com._Blog.Backend.services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com._Blog.Backend.dto.ReportRequest;
import com._Blog.Backend.dto.UserAccountUpdateRequest;
import com._Blog.Backend.dto.UserProfileUpdateRequest;
import com._Blog.Backend.dto.UserResponse;
import com._Blog.Backend.dto.UserSuggetion;
import com._Blog.Backend.exception.BadRequestException;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.model.JwtUser;
import com._Blog.Backend.model.ReportUser;
import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.ReportUserRepository;
import com._Blog.Backend.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReportUserRepository reportUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowService followService;

    public UserResponse GetUser() {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(jwtUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserResponse(user);
    }

    public UserResponse GetUserById(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserResponse(user);
    }

    public void reportUser(ReportRequest reportRequest) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User reporter = this.userRepository.findById(jwtUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User reported = this.userRepository.findById(reportRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ReportUser reportUser = new ReportUser(reporter, reported, reportRequest.getDescription());
        this.reportUserRepository.save(reportUser);
    }

    public UserResponse updateProfile(UserProfileUpdateRequest profileUpdateRequest) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = this.userRepository.findById(jwtUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

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
        User user = this.userRepository.findById(jwtUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (accountUpdateRequest.getOldPassword() == null ||
                !passwordEncoder.matches(accountUpdateRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Wrong password");
        }
        if (accountUpdateRequest.getEmail() == null || !user.getEmail().equals(accountUpdateRequest.getEmail())) {
            throw new BadRequestException("Wrong email");
        }
        if (accountUpdateRequest.getNewPassword() != null) {
            user.setPassword(passwordEncoder.encode(accountUpdateRequest.getNewPassword()));
        }
        User savedUser = this.userRepository.save(user);
        return new UserResponse(savedUser);
    }

    public List<UserSuggetion> getUsersSuggetion(Integer page) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(page, 10);
        return this.userRepository.findAllByIsBannedAndIdNot(false, jwtUser.getId(), pageable).getContent().stream()
                .map(u -> new UserSuggetion(new UserResponse(u), this.followService.isFollowing(u.getId()))).toList();
    }
}
