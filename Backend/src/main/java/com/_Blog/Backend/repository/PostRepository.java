package com._Blog.Backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._Blog.Backend.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT * FROM post " +
            "WHERE user_id IN :userIds AND is_hide = false " +
            "ORDER BY create_time DESC " +
            "LIMIT 10 OFFSET :offset",
            nativeQuery = true)
    List<Post> findPostsByUserIds(@Param("userIds") List<Long> userIds, @Param("offset") Long offset);
    @Query(value = "SELECT * FROM post " +
            "WHERE user_id NOT IN :userIds AND is_hide = false " +
            "ORDER BY RANDOM() LIMIT 10",
            nativeQuery = true)
    List<Post> findRandomPostsExcludingUsers(@Param("userIds") List<Long> userIds);

    @Query(value = "SELECT * FROM post WHERE is_hide = false ORDER BY create_time DESC LIMIT 10 OFFSET :offset", nativeQuery = true)
    List<Post> findNewestPosts(@Param("offset") Long offset);
}
