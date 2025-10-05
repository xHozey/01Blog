package com._Blog.Backend.dto;

import com._Blog.Backend.model.Comment;

import java.sql.Timestamp;

public class CommentResponse {
    private Long id;
    private String content;
    private String author;
    private Long authorId;
    private Timestamp createTime;
    private Long likes;
    private Boolean isLiked;

    public CommentResponse(Long id, String content, String author, Long authorId, Timestamp createTime, Long likes, Boolean isLiked) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.authorId = authorId;
        this.createTime = createTime;
        this.likes = likes;
        this.isLiked = isLiked;
    }

    public CommentResponse(Comment comment, Long likes, Boolean isLiked) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getUser().getUsername();
        this.authorId = comment.getUser().getId();
        this.createTime = comment.getCreateTime();
        this.likes = likes;
        this.isLiked = isLiked;
    }

    public CommentResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Boolean liked) {
        isLiked = liked;
    }

}
