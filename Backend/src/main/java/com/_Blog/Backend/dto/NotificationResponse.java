package com._Blog.Backend.dto;

import com._Blog.Backend.model.Notification;

import java.sql.Timestamp;

public class NotificationResponse {
    private Long id;
    private String description;
    private java.sql.Timestamp createdTime;
    private Boolean isRead;

    public NotificationResponse(Long id, String description, Timestamp createdTime, Boolean isRead) {
        this.id = id;
        this.description = description;
        this.createdTime = createdTime;
        this.isRead = isRead;
    }

    public NotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.description = notification.getDescription();
        this.createdTime = notification.getCreatedAt();
        this.isRead = notification.getIsRead();
    }

    public NotificationResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean read) {
        isRead = read;
    }
}
