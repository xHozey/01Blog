package com._Blog.Backend.controller;

import com._Blog.Backend.dto.NotificationResponse;
import com._Blog.Backend.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications(@RequestParam(defaultValue = "0") Integer page) {
        List<NotificationResponse> notifications = this.notificationService.getNotifications(page);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/read")
    public ResponseEntity<String> readNotifications(@RequestBody List<Long> ids) {
        this.notificationService.readNotifications(ids);
        return ResponseEntity.ok("notifications readed");
    }
}
