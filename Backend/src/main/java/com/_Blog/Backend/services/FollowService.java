package com._Blog.Backend.services;

import com._Blog.Backend.exception.ConflictException;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.model.Follow;
import com._Blog.Backend.model.JwtUser;
import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.FollowRepository;
import com._Blog.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Autowired
    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public void addFollow(Long followedId) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (followedId.equals(jwtUser.getId())) throw new ConflictException("you can't follow yourself");
        User followerUser = userRepository.findById(jwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException("follower user not found"));
        User followedUser = userRepository.findById(followedId).orElseThrow(() -> new ResourceNotFoundException("followed user not found"));
        Follow follow = new Follow();
        follow.setFollower(followerUser);
        follow.setFollowed(followedUser);
        followRepository.save(follow);
    }

    public void removeFollow(Long followedId) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (followedId.equals(jwtUser.getId())) throw new ConflictException("you can't unfollow yourself");
        Follow follow = followRepository.findByFollowedIdAndFollowerId(jwtUser.getId(), followedId).orElseThrow(() -> new ResourceNotFoundException("followed user not found"));
        followRepository.delete(follow);
    }

    public Long getFollowersTotal() {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return followRepository.countByFollowedId(jwtUser.getId());
    }

    public Long getFollowedTotal() {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return followRepository.countByFollowerId(jwtUser.getId());
    }
}