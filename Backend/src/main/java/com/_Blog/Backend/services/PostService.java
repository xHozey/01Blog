package com._Blog.Backend.services;

import java.util.List;

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

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post addPost(Post post) {
        return this.postRepository.save(post);
    }

    public List<Post> getPosts(Long offset) {
        return postRepository.findPostsByOffsetLimit(offset * 10);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Post not found with id %d", id)));
    }

    public Post updatePost(Post post) {
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (post.getId() == null) throw new BadRequestException("Post id is null");
        Post oldPost = postRepository.findById(post.getId()).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        if (!user.getId().equals(oldPost.getUserId())) throw new UnauthorizedException("You are not allowed to update this post");

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
        if (!post.getUserId().equals(currentUserId) && !isAdmin) {
            throw new UnauthorizedException("You are not allowed to delete this post");
        }

        postRepository.delete(post);
    }
}
