package com._Blog.Backend.dto;

import java.sql.Timestamp;

import com._Blog.Backend.model.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private String author;
    private Long authorId;
    private Timestamp createdAt;
    private Long likes;
    private Boolean isLiked;

    public PostResponse(Post post, Long likes, Boolean isLiked) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getUser().getUsername();
        this.authorId = post.getUser().getId();
        this.createdAt = post.getCreatedAt();
        this.likes = likes;
        this.isLiked = isLiked;
    }

}
