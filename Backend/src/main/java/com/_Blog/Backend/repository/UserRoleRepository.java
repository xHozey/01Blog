package com._Blog.Backend.repository;

import com._Blog.Backend.model.User;
import com._Blog.Backend.utils.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._Blog.Backend.model.UserRole;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long>{
    void deleteByUserAndRole(User user, Role role);
}
