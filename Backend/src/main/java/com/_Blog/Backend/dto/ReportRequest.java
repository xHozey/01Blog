package com._Blog.Backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReportRequest {
    @NotNull
    private Long postId;
    @NotBlank
    @Size(min = 1, max = 250)
    private String description;

    public ReportRequest(Long postId, String description) {
        this.postId = postId;
        this.description = description;
    }

    public ReportRequest() {}

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
