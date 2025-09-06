package com._Blog.Backend.model;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;
    private String iconPath;
    private String token;
    private Boolean isBanned;
    @CreationTimestamp
    private Timestamp creation;

    public User() {
    }

    public User(Long id, String username, String email, String password, String role, String iconPath, String token, Boolean isBanned, Timestamp creation) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.iconPath = iconPath;
        this.token = token;
        this.isBanned = isBanned;
        this.creation = creation;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(Boolean isBanned) {
        this.isBanned = isBanned;
    }

    public Timestamp getCreation() {
        return creation;
    }

    public void setCreation(Timestamp creation) {
        this.creation = creation;
    }
}
