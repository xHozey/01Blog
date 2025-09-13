package com._Blog.Backend.model;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "follow",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"followerId", "followedId"})})

public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", updatable = false)
    private User follower;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", updatable = false)
    private User followed;

    public Follow() {
    }

    public Follow(Long id, User follower, User followed) {
        this.id = id;
        this.followed = followed;
        this.follower = follower;
    }

    public User getFollowed() {
        return this.followed;
    }

    public User getFollower() {
        return this.follower;
    }

    public Long getId() {
        return this.id;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public void setFollowed(User followed) {
        this.followed = followed;
    }

}
