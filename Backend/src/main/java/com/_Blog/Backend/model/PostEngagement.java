package com._Blog.Backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "post_engagement")
public class PostEngagement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long postId;
    private Long userId;
    private Boolean likeType;

    public PostEngagement() {
    }

    public PostEngagement(Long id, Long postId, Long userId, Boolean likeType) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.likeType = likeType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getLikeType() {
        return likeType;
    }

    public void setLikeType(Boolean likeType) {
        this.likeType = likeType;
    }
}
