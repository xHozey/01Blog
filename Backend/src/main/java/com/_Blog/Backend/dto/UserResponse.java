package com._Blog.Backend.dto;

import java.sql.Timestamp;

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

    public UserResponse() {}

    public String getName() {
        return username;
    }

    public void setName(String username) {
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
