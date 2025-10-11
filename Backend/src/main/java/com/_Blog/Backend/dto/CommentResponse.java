package com._Blog.Backend.dto;

import com._Blog.Backend.model.Comment;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private String author;
    private Long authorId;
    private Timestamp createTime;
    private Long likes;
    private Boolean isLiked;

    public CommentResponse(Comment comment, Long likes, Boolean isLiked) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getUser().getUsername();
        this.authorId = comment.getUser().getId();
        this.createTime = comment.getCreateTime();
        this.likes = likes;
        this.isLiked = isLiked;
    }
    
}
