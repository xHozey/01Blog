package com._Blog.Backend.model;

public class PostMedia {
    private Long id;
    private String fileUrl;
    private Post post;

    public PostMedia(Long id, String fileUrl, Post post) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.post = post;
    }

    public PostMedia(String fileUrl, Post post) {
        this.fileUrl = fileUrl;
        this.post = post;
    }

    public PostMedia() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
