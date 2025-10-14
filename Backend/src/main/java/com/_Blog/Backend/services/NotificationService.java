package com._Blog.Backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com._Blog.Backend.dto.NotificationResponse;
import com._Blog.Backend.model.Follow;
import com._Blog.Backend.model.JwtUser;
import com._Blog.Backend.model.Notification;
import com._Blog.Backend.model.Post;
import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.FollowRepository;
import com._Blog.Backend.repository.NotificationRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FollowRepository followRepository;
    
    @Async
    public void notifyFollowers(User sender, String description, Post post) {
        List<Follow> followers = followRepository.findByFollowedId(sender.getId());
        List<Notification> notifications = followers.stream().map(f -> {
            Notification n = new Notification();
            n.setSender(sender);
            n.setReceiver(f.getFollower());
            n.setDescription(description);
            n.setPost(post);
            return n;
        }).toList();
        notificationRepository.saveAll(notifications);
    }

    public List<NotificationResponse> getNotifications(Integer page) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        return this.notificationRepository.findByReceiverId(jwtUser.getId(), pageable).stream()
                .map(NotificationResponse::new).toList();
    }

    public void readNotifications(List<Long> ids) {
        for (Long id : ids) {
            Optional<Notification> notification = notificationRepository.findById(id);
            if (notification.isPresent()) {
                Notification n = notification.get();
                n.setIsRead(true);
                this.notificationRepository.save(n);
            }
        }
    }

    public Long countNotifications() {
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.notificationRepository.countByReceiverIdAndIsReadFalse(user.getId());
    }
}
