package com._Blog.Backend.dto;

import java.sql.Timestamp;

public class UserResponse {
    private String name;
    private String iconPath;
    private Timestamp creation;

    public UserResponse(String name, String iconPath, Timestamp creation) {
        this.name = name;
        this.iconPath = iconPath;
        this.creation = creation;
    }

    public UserResponse() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
