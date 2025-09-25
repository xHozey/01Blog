package com._Blog.Backend.repository;

import com._Blog.Backend.model.CommentEngagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentEngagementRepository extends JpaRepository<CommentEngagement,Long> {
    Optional<CommentEngagement> findByCommentIdAndUserId(Long postId, Long userId);

}
