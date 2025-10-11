package com._Blog.Backend.dto;

import java.sql.Timestamp;

import com._Blog.Backend.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {

    private Long id;
    private String username;
    private String iconPath;
    private Timestamp creation;
    private String[] roles;
    private String bio;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.iconPath = user.getIconPath();
        this.creation = user.getCreateAt();
        this.roles = user.getRoles().stream().map(r -> r.getRole().name()).toArray(String[]::new);
        this.bio = user.getBio();
    }
}
