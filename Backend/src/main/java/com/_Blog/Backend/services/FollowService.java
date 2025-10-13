package com._Blog.Backend.services;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com._Blog.Backend.exception.ConflictException;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.model.Follow;
import com._Blog.Backend.model.JwtUser;
import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.FollowRepository;
import com._Blog.Backend.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public void toggleFollow(Long followedId) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (followedId.equals(jwtUser.getId())) {
            throw new ConflictException("you can't follow yourself");
        }

        User followerUser = userRepository.findById(jwtUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("follower user not found"));
        User followedUser = userRepository.findById(followedId)
                .orElseThrow(() -> new ResourceNotFoundException("followed user not found"));

        Optional<Follow> existingFollow = followRepository.findByFollowerIdAndFollowedId(jwtUser.getId(), followedId);

        if (existingFollow.isPresent()) {
            followRepository.delete(existingFollow.get());
        } else {
            Follow follow = new Follow();
            follow.setFollower(followerUser);
            follow.setFollowed(followedUser);
            followRepository.save(follow);
        }
    }

    public Boolean isFollowing(Long followedId) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (followedId.equals(jwtUser.getId())) {
            return false;
        }
        return followRepository.existsByFollowerIdAndFollowedId(jwtUser.getId(), followedId);
    }

    public Long getFollowersTotal(Long userId) {
        return followRepository.countByFollowedId(userId);
    }

    public Long getFollowedTotal(Long userId) {
        return followRepository.countByFollowerId(userId);
    }
}
