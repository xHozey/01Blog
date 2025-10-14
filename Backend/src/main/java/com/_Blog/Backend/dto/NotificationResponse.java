package com._Blog.Backend.dto;

import com._Blog.Backend.model.Notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationResponse {
    private Long id;
    private String description;
    private java.sql.Timestamp createdTime;
    private Long postId;
    private Boolean isRead;

    public NotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.description = notification.getDescription();
        this.createdTime = notification.getCreatedAt();
        this.isRead = notification.getIsRead();
        this.postId = notification.get
    }

}
