package com._Blog.Backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._Blog.Backend.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByUserIdInAndIsHideFalse(List<Long> userIds, Pageable pageable);
    Page<Post> findByUserIdAndIsHideFalse(Long userId, Pageable pageable);
    Page<Post> findAllByIsHideFalse(Pageable pageable);
}

