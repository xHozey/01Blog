package com._Blog.Backend.services;

import org.springframework.stereotype.Service;

import com._Blog.Backend.model.User;
import com._Blog.Backend.model.UserRole;
import com._Blog.Backend.repository.UserRoleRepository;
import com._Blog.Backend.utils.Role;

@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public void addRole(Role role, User user) {
        UserRole userRole = new UserRole(user, role);
        user.getRoles().add(userRole);
        userRoleRepository.save(userRole);
    }
}