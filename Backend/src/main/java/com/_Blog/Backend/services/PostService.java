package com._Blog.Backend.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com._Blog.Backend.dto.PostRequest;
import com._Blog.Backend.dto.PostResponse;
import com._Blog.Backend.dto.ReportRequest;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.exception.UnauthorizedException;
import com._Blog.Backend.model.JwtUser;
import com._Blog.Backend.model.Post;
import com._Blog.Backend.model.ReportPost;
import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.FollowRepository;
import com._Blog.Backend.repository.PostEngagementRepository;
import com._Blog.Backend.repository.PostRepository;
import com._Blog.Backend.repository.ReportPostRepository;
import com._Blog.Backend.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService {

        private final PostRepository postRepository;
        private final UserRepository userRepository;
        private final FollowRepository followRepository;
        private final PostEngagementRepository postEngagementRepository;
        private final NotificationService notificationService;
        private final ReportPostRepository reportPostRepository;

        public PostResponse addPost(PostRequest post) {
                JwtUser JwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                User user = userRepository.findById(JwtUser.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                Post newPost = new Post(post.getTitle(), post.getContent(), user);

                Post savedPost = this.postRepository.save(newPost);
                this.notificationService.notifyFollowers(user,
                                String.format("%s has posted new blog", user.getUsername()),
                                savedPost);
                return new PostResponse(savedPost, this.postEngagementRepository.countByPostId(savedPost.getId()),
                                this.postEngagementRepository.existsByPostIdAndUserId(savedPost.getId(),
                                                JwtUser.getId()));
        }

        public List<PostResponse> getUserPosts(Long userId, Integer page) {
                JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
                return this.postRepository
                                .findByUserIdAndIsHideFalse(userId, pageable)
                                .map(p -> new PostResponse(p, postEngagementRepository.countByPostId(p.getId()),
                                                postEngagementRepository.existsByPostIdAndUserId(p.getId(),
                                                                jwtUser.getId())))
                                .getContent();
        }

        public List<PostResponse> getPosts(Integer page) {
                JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                List<Long> followedUserIds = followRepository.findByFollowerId(jwtUser.getId())
                                .stream()
                                .map(follow -> follow.getFollowed().getId())
                                .toList();

                Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

                Page<Post> postsPage;

                if (!followedUserIds.isEmpty()) {
                        followedUserIds = new ArrayList<>(followedUserIds);
                        followedUserIds.add(jwtUser.getId());

                        postsPage = postRepository.findAllByUserIdInAndIsHideFalse(followedUserIds, pageable);
                } else {
                        postsPage = postRepository.findAllByIsHideFalse(pageable);
                }

                return postsPage.getContent().stream()
                                .map(p -> new PostResponse(p, postEngagementRepository.countByPostId(p.getId()),
                                                postEngagementRepository.existsByPostIdAndUserId(p.getId(),
                                                                jwtUser.getId())))
                                .toList();
        }

        public PostResponse getPostById(Long id) {
                JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Post post = postRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                String.format("Post not found with id %d", id)));
                return new PostResponse(post, this.postEngagementRepository.countByPostId(post.getId()),
                                this.postEngagementRepository.existsByPostIdAndUserId(post.getId(), jwtUser.getId()));
        }

        public PostResponse updatePost(PostRequest post, Long postId) {
                JwtUser JwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                User user = userRepository.findById(JwtUser.getId()).orElseThrow(
                                () -> new ResourceNotFoundException(
                                                String.format("User not found with id %d", JwtUser.getId())));
                Post oldPost = postRepository.findById(postId)
                                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
                if (!user.getId().equals(oldPost.getUser().getId())) {
                        throw new UnauthorizedException("You are not allowed to update this post");
                }

                oldPost.setTitle(post.getTitle());
                oldPost.setContent(post.getContent());

                Post savedPost = this.postRepository.save(oldPost);
                return new PostResponse(savedPost, this.postEngagementRepository.countByPostId(savedPost.getId()),
                                this.postEngagementRepository.existsByPostIdAndUserId(savedPost.getId(), user.getId()));
        }

        public void deletePost(Long postId) {
                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                String.format("Post not found with id %d", postId)));
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                JwtUser currentUser = (JwtUser) auth.getPrincipal();
                Long currentUserId = currentUser.getId();

                if (!post.getUser().getId().equals(currentUserId)) {
                        throw new UnauthorizedException("You are not allowed to delete this post");
                }

                postRepository.delete(post);
        }

        public void reportPost(ReportRequest reportRequest, Long id) {
                JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                User reporter = this.userRepository.findById(jwtUser.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("reporter not found"));
                User reported = this.userRepository.findById(reportRequest.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("reported user not found"));
                Post post = this.postRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("post not found"));
                this.reportPostRepository
                                .save(new ReportPost(reporter, reported, post, reportRequest.getDescription()));
        }

}
