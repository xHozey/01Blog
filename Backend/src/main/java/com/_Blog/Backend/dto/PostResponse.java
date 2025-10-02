package com._Blog.Backend.dto;

import java.sql.Timestamp;

public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private Long authorId;
    private Timestamp createTime;
    private Long likes;
    private Boolean isLiked;
    private String filePath;

    public PostResponse(Long id, String title, String content, String author, Long authorId, String filePath, Timestamp createTime, Long likes, Boolean isLiked) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.authorId = authorId;
        this.createTime = createTime;
        this.likes = likes;
        this.isLiked = isLiked;
        this.filePath = filePath;
    }

    public PostResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
