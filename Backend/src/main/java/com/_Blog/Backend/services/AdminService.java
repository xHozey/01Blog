package com._Blog.Backend.services;

import com._Blog.Backend.dto.AdminPostDTO;
import com._Blog.Backend.dto.AdminUserDTO;
import com._Blog.Backend.dto.PostResponse;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.model.*;
import com._Blog.Backend.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostEngagementRepository postEngagementRepository;
    private final ReportUserRepository reportUserRepository;
    private final ReportPostRepository reportPostRepository;
    private final ReportCommentRepository reportCommentRepository;

    AdminService(PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository, PostEngagementRepository postEngagementRepository, ReportUserRepository reportUserRepository, ReportPostRepository reportPostRepository,  ReportCommentRepository reportCommentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postEngagementRepository = postEngagementRepository;
        this.reportUserRepository = reportUserRepository;
        this.reportPostRepository = reportPostRepository;
        this.reportCommentRepository = reportCommentRepository;
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

    public List<ReportUser> getUserReports(Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("username").descending());
        return this.reportUserRepository.findAll(pageable).toList();
    }

    public List<ReportPost> getPostReports(Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC,"createAt"));
        return this.reportPostRepository.findAll(pageable).toList();
    }

    public List<ReportComment> getCommentReports(Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        return this.reportCommentRepository.findAll(pageable).toList();
    }
}
