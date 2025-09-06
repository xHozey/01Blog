package com._Blog.Backend.repository;
import org.springframework.stereotype.Repository;

import com._Blog.Backend.model.Comment;

@Repository
public interface CommentRepo {

    int insertComment(Comment comment);
}
