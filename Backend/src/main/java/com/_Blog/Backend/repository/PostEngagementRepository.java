package com._Blog.Backend.repository;

import com._Blog.Backend.model.PostEngagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostEngagementRepository extends JpaRepository<PostEngagement, Long> {
    Optional<PostEngagement> findByPostIdAndUserId(Long postId, Long userId);
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    Long countByPostId(Long postId);
}
