package com._Blog.Backend.repository;

import com._Blog.Backend.model.Follow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends CrudRepository<Follow,Long> {
    Optional<Follow> findByFollowerIdAndFollowedId(Long followedId, Long followerId);
    Long countByFollowedId(Long followedId);
    Long countByFollowerId(Long followerId);
    List<Follow> findByFollowerId(Long followerId);
    List<Follow> findByFollowedId(Long followedId);
    Boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);
}


