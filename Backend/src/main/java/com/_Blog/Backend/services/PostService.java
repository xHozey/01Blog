package com._Blog.Backend.services;

import java.util.List;

import com._Blog.Backend.model.User;
import com._Blog.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com._Blog.Backend.exception.BadRequestException;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.exception.UnauthorizedException;
import com._Blog.Backend.model.JwtUser;
import com._Blog.Backend.model.Post;
import com._Blog.Backend.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post addPost(Post post) {
        JwtUser JwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(JwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        post.setUser(user);
        return this.postRepository.save(post);
    }

    public List<Post> getPosts(Long offset) {
        return postRepository.findPostsByOffsetLimit(offset * 10);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Post not found with id %d", id)));
    }

    public Post updatePost(Post post) {
        JwtUser JwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user =  userRepository.findById(JwtUser.getId()).orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with id %d", JwtUser.getId())));
        if (post.getId() == null) throw new BadRequestException("Post id is null");
        Post oldPost = postRepository.findById(post.getId()).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        if (!user.getId().equals(oldPost.getUser().getId())) throw new UnauthorizedException("You are not allowed to update this post");

        oldPost.setTitle(post.getTitle());   
        oldPost.setContent(post.getContent());
        oldPost.setImagePath(post.getImagePath());
        oldPost.setVideoPath(post.getVideoPath());
        return postRepository.save(oldPost);
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Post not found with id %d", postId)));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JwtUser currentUser = (JwtUser) auth.getPrincipal();
        Long currentUserId = currentUser.getId();
        boolean isAdmin = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
        if (!post.getUser().getId().equals(currentUserId) && !isAdmin) {
            throw new UnauthorizedException("You are not allowed to delete this post");
        }

        postRepository.delete(post);
    }
}
