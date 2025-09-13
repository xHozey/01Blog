package com._Blog.Backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comment_engagement")
public class CommentEngagement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "comment_id")
    private Comment comment;
    private Boolean likeType;

    public CommentEngagement() {
    }

    public CommentEngagement(Long id, User user, Comment comment, Boolean likeType) {
        this.id = id;
        this.user = user;
        this.comment = comment;
        this.likeType = likeType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Boolean getLikeType() {
        return likeType;
    }

    public void setLikeType(Boolean likeType) {
        this.likeType = likeType;
    }
}
