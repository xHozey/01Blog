package com._Blog.Backend.repository;

import com._Blog.Backend.model.Follow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends CrudRepository<Follow,Integer> {
    Optional<Follow> findByFollowedIdAndFollowerId(Long followedId, Long followerId);
}