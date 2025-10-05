package com._Blog.Backend.services;

import com._Blog.Backend.dto.PostResponse;
import com._Blog.Backend.exception.ResourceNotFoundException;
import com._Blog.Backend.model.Comment;
import com._Blog.Backend.model.Post;
import com._Blog.Backend.repository.CommentRepository;
import com._Blog.Backend.repository.PostRepository;
import com._Blog.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    AdminService(PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public void DeletePost(Long postId) {
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

    public List<PostResponse> getPosts() {

    }
}
