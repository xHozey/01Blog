package com._Blog.Backend.dto;

import com._Blog.Backend.model.User;

import java.sql.Timestamp;

public class AdminUserDTO {
        private Long id;
        private String username;
        private String email;
        private String iconPath;
        private Boolean isBanned;
        private Timestamp creation;

    public AdminUserDTO(Long id, String username, String email, String iconPath, Boolean isBanned, Timestamp creation) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.iconPath = iconPath;
        this.isBanned = isBanned;
        this.creation = creation;
    }

    public AdminUserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.iconPath = user.getIconPath();
        this.isBanned = user.getIsBanned();
        this.creation = user.getCreation();
    }

    public AdminUserDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public Boolean getBanned() {
        return isBanned;
    }

    public void setBanned(Boolean banned) {
        isBanned = banned;
    }

    public Timestamp getCreation() {
        return creation;
    }

    public void setCreation(Timestamp creation) {
        this.creation = creation;
    }
}
