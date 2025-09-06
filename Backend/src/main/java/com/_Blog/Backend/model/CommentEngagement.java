package com._Blog.Backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "comment_engagement")
public class CommentEngagement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private Long userId;
    private Long commentId;
    private Boolean likeType;

    public CommentEngagement() {
    }

    public CommentEngagement(Long id, Long userId, Long commentId, Boolean likeType) {
        this.id = id;
        this.userId = userId;
        this.commentId = commentId;
        this.likeType = likeType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Boolean getLikeType() {
        return likeType;
    }

    public void setLikeType(Boolean likeType) {
        this.likeType = likeType;
    }
}
