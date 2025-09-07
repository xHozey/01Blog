package com._Blog.Backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._Blog.Backend.exception.ResourceNotFoundException;
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

    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Post not found with id %d", postId)));

        if (!post.getUserId().equals(userId)) {
            return;
        }

        postRepository.delete(post);
    }
}
