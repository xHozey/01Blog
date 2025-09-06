package com._Blog.Backend.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._Blog.Backend.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT * FROM comment ORDER BY id LIMIT 10 OFFSET :offset", nativeQuery = true)
    List<Comment> findCommentsByOffsetLimit(@Param("offset") Long offset);
}
