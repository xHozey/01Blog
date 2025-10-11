package com._Blog.Backend.dto;

import java.sql.Timestamp;

import com._Blog.Backend.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminUserDTO {

    private Long id;
    private String username;
    private String email;
    private String iconPath;
    private Boolean isBanned;
    private Timestamp createdAt;

    public AdminUserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.iconPath = user.getIconPath();
        this.isBanned = user.getIsBanned();
        this.createdAt = user.getCreateAt();
    }
}
