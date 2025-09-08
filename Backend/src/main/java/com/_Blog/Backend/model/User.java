package com._Blog.Backend.model;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Size(max = 30, min = 3)
    @Column(unique=true)
    private String username;
    @NotBlank
    @Email
    @Size(max=255)
    @Column(unique=true)
    private String email;
    @NotBlank
    @Size(max = 64, min = 8)
    @Column(unique=true)
    private String password;
    private String role = "USER";
    private String iconPath;
    private String token;
    private Boolean isBanned = false;
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
    
    @Override
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

    @Override
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Map your role field to a GrantedAuthority
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // adjust if you want expiry logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return !Boolean.TRUE.equals(this.isBanned); // banned users are "locked"
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // adjust if you want password expiry
    }

    @Override
    public boolean isEnabled() {
        return !Boolean.TRUE.equals(this.isBanned);
    }
}
