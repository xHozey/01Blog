package com._Blog.Backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._Blog.Backend.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT * FROM post ORDER BY id LIMIT 10 OFFSET :offset", nativeQuery = true)
    List<Post> findPostsByOffsetLimit(@Param("offset") Long offset);
}
