package com._Blog.Backend.dto;

import java.sql.Timestamp;

public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String videoPath;
    private String imagePath;
    private Timestamp createTime;
    private Long likes;
    private Boolean isLiked;

    public PostResponse(Long id, String title, String content, String author, String videoPath, String imagePath, Timestamp createTime, Long likes, Boolean isLiked) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.videoPath = videoPath;
        this.imagePath = imagePath;
        this.createTime = createTime;
        this.likes = likes;
        this.isLiked = isLiked;
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

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }
}
