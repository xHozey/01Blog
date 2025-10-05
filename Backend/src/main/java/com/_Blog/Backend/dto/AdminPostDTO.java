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


}
