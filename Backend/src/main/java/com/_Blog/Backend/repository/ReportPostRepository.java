package com._Blog.Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com._Blog.Backend.model.ReportPost;

public interface ReportPostRepository extends JpaRepository<ReportPost, Long> {
}
