package com._Blog.Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._Blog.Backend.model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long>{
}
