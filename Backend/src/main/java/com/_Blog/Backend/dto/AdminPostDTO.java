package com._Blog.Backend.dto;

import com._Blog.Backend.model.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminPostDTO {

    private Long id;
    private String title;
    private String content;
    private Boolean isHide;
    private Long authorId;
    private String authorName;
    private Long likes;
    private Timestamp createdAt;
    public AdminPostDTO(Post post, Long likes) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.isHide = post.getIsHide();
        this.authorId = post.getUser().getId();
        this.authorName = post.getUser().getUsername();
        this.likes = likes;
        this.createdAt = post.getCreatedAt();
    }
}
