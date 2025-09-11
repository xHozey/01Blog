package com._Blog.Backend.model;

public class JwtUser {
    private final Long id;
    private final String username;

    public JwtUser(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
}
