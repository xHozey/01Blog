package com._Blog.Backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserProfileUpdateRequest {
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    private String password;
    @NotBlank
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    private String confirmPassword;
    @Size(max = 1500)
    private String iconProfile;

    public UserProfileUpdateRequest(String username, String password, String confirmPassword, String iconProfile) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.iconProfile = iconProfile;
    }

    public  UserProfileUpdateRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getIconProfile() {
        return iconProfile;
    }

    public void setIconProfile(String iconProfile) {
        this.iconProfile = iconProfile;
    }
}
