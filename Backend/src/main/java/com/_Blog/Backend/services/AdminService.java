package com._Blog.Backend.services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com._Blog.Backend.dto.AdminPostDTO;
import com._Blog.Backend.dto.AdminUserDTO;
import com._Blog.Backend.dto.PostReportDTO;
import com._Blog.Backend.dto.UserReportDTO;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.model.Post;
import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.CommentRepository;
import com._Blog.Backend.repository.PostEngagementRepository;
import com._Blog.Backend.repository.PostRepository;
import com._Blog.Backend.repository.ReportPostRepository;
import com._Blog.Backend.repository.ReportUserRepository;
import com._Blog.Backend.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostEngagementRepository postEngagementRepository;
    private final ReportUserRepository reportUserRepository;
    private final ReportPostRepository reportPostRepository;

    public void deletePost(Long postId) {
        this.postRepository.deleteById(postId);
    }

    public void hidePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        if (post.getIsHide()) {
            post.setIsHide(false);
        } else {
            post.setIsHide(true);
        }
        postRepository.save(post);
    }

    public void deleteComment(Long commentId) {
        this.commentRepository.deleteById(commentId);
    }

    public List<AdminPostDTO> getPosts(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.postRepository.findAll(pageable)
                .map(post -> new AdminPostDTO(post, this.postEngagementRepository.countByPostId(post.getId())))
                .toList();
    }

    public void banUser(Long id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getIsBanned()) {
            user.setIsBanned(false);
        } else {
            user.setIsBanned(true);
        }
        userRepository.save(user);
    }

    public List<AdminUserDTO> getUsers(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.userRepository.findAll(pageable).map(AdminUserDTO::new).toList();
    }

    public List<AdminUserDTO> getBannedUsers(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.userRepository.findAllByIsBanned(true, pageable).map(AdminUserDTO::new).toList();
    }

    public List<UserReportDTO> getUserReports(Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createAt").descending());
        return this.reportUserRepository.findAll(pageable).stream().map(UserReportDTO::new).toList();
    }

    public void deleteUser(Long userId) {
        this.userRepository.deleteById(userId);
    }

    public Long countUsers() {
        return this.userRepository.count();
    }

    public Long countPosts() {
        return this.postRepository.count();
    }

    public Long countUserReports() {
        return this.reportUserRepository.count();
    }

    public Long countPostReports() {
        return this.reportPostRepository.count();
    }

    public Long countBannedUsers() {
        return this.userRepository.countByIsBanned(true);
    }

    public List<PostReportDTO> getPostReports(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.reportPostRepository.findAll(pageable).getContent().stream().map(r -> new PostReportDTO(r))
                .toList();
    }
}
