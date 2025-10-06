package com._Blog.Backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._Blog.Backend.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByUserIdInAndIsHideFalse(List<Long> userIds, Pageable pageable);

    @Query(value = "SELECT * FROM post " +
            "WHERE user_id NOT IN :userIds AND is_hide = false " +
            "ORDER BY RANDOM()",
            countQuery = "SELECT COUNT(*) FROM post WHERE user_id NOT IN :userIds AND is_hide = false",
            nativeQuery = true)
    Page<Post> findRandomPostsExcludingUsers(@Param("userIds") List<Long> userIds, Pageable pageable);

    Page<Post> findAllByIsHideFalse(Pageable pageable);
}

