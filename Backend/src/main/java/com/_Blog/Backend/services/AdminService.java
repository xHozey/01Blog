package com._Blog.Backend.services;

import com._Blog.Backend.dto.AdminPostDTO;
import com._Blog.Backend.dto.AdminUserDTO;
import com._Blog.Backend.dto.PostResponse;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.model.Post;
import com._Blog.Backend.model.PostEngagement;
import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.CommentRepository;
import com._Blog.Backend.repository.PostEngagementRepository;
import com._Blog.Backend.repository.PostRepository;
import com._Blog.Backend.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostEngagementRepository postEngagementRepository;

    AdminService(PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository, PostEngagementRepository postEngagementRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postEngagementRepository = postEngagementRepository;
    }

    public void deletePost(Long postId) {
        this.postRepository.deleteById(postId);
    }

    public void hidePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found"));
        post.setIsHide(true);
        postRepository.save(post);
    }

    public void deleteComment(Long commentId) {
        this.commentRepository.deleteById(commentId);
    }

    public List<AdminPostDTO> getPosts(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.postRepository.findAll(pageable).map(post -> new AdminPostDTO(post, this.postEngagementRepository.countByPostId(post.getId()))).toList();
    }

    public void banUser(Long id) {
        User user = this.userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
        user.setIsBanned(true);
        userRepository.save(user);
    }

    public void unBanUser(Long id) {
        User user = this.userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
        user.setIsBanned(false);
        userRepository.save(user);
    }

    public List<AdminUserDTO> getUsers(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.userRepository.findAll(pageable).map(AdminUserDTO::new).toList();
    }

    public List<AdminUserDTO> getBannedUsers(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.userRepository.findAllByIsBanned(true,  pageable).map(AdminUserDTO::new).toList();
    }
}
