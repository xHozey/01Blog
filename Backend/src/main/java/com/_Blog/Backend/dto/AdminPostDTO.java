package com._Blog.Backend.dto;

import com._Blog.Backend.model.Post;
import org.springframework.security.core.parameters.P;

public class AdminPostDTO {
    private Long id;
    private String title;
    private String content;
    private Boolean isHide;
    private Long authorId;
    private String authorName;
    private Long likes;

    public AdminPostDTO(Long id, String title, String content, Boolean isHide, Long authorId, String authorName, Long likes) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isHide = isHide;
        this.authorId = authorId;
        this.authorName = authorName;
        this.likes = likes;
    }

    public AdminPostDTO(Post post, Long likes) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.isHide = post.getIsHide();
        this.authorId = post.getUser().getId();
        this.authorName = post.getUser().getUsername();
        this.likes = likes;
    }

    public AdminPostDTO() {}


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

    public Boolean getHide() {
        return isHide;
    }

    public void setHide(Boolean hide) {
        isHide = hide;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }
}
