package com._Blog.Backend.dto;

import java.sql.Timestamp;

import com._Blog.Backend.model.User;

public class UserResponse {
    private Long id;
    private String username;
    private String iconPath;
    private Timestamp creation;
    private String[] roles;

    public UserResponse(Long id, String username, String iconPath, Timestamp creation, String[] roles) {
        this.id = id;
        this.username = username;
        this.iconPath = iconPath;
        this.creation = creation;
        this.roles = roles;
    }

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.iconPath = user.getIconPath();
        this.creation = user.getCreateAt();
        this.roles = user.getRoles().stream().map(r -> r.getRole().name()).toArray(String[]::new);
    }

    public UserResponse() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public Timestamp getCreation() {
        return creation;
    }

    public void setCreation(Timestamp creation) {
        this.creation = creation;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
