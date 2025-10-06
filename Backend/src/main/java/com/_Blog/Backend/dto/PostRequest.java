package com._Blog.Backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostRequest {
    @NotBlank
    @Size(max = 300)
    private String title;
    @NotBlank
    @Size(max = 10000)
    private String content;
    @Size(max = 300)
    private String[] media;

    public PostRequest(String title, String content, String[] media) {
        this.title = title;
        this.content = content;
        this.media = media;
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

}
