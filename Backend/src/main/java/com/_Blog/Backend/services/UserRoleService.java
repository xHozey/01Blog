package com._Blog.Backend.services;

import org.springframework.stereotype.Service;

import com._Blog.Backend.model.User;
import com._Blog.Backend.model.UserRole;
import com._Blog.Backend.repository.UserRoleRepository;
import com._Blog.Backend.utils.Role;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public void addRole(Role role, User user) {
        UserRole userRole = new UserRole(user, role);
        user.getRoles().add(userRole);
        userRoleRepository.save(userRole);
    }

    public void deleteRole(Role role, User user) {
        this.userRoleRepository.deleteByUserAndRole(user, role);
    }
}