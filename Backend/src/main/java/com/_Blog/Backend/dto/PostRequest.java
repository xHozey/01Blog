package com._Blog.Backend.dto;

public class PostRequest {
    private String title;
    private String content;
    private String videoPath;
    private String imagePath;

    public PostRequest(String title, String content, String videoPath, String imagePath) {
        this.title = title;
        this.content = content;
        this.videoPath = videoPath;
        this.imagePath = imagePath;
    }

    public PostRequest() {}

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
}
