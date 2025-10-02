package com._Blog.Backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "follow",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"followerId", "followedId"})})

public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", updatable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_id", updatable = false)
    private User followed;

    public Follow() {}
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

    public void setId(Long id) {
        this.id = id;
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
