package com._Blog.Backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "post_engagement")
public class PostEngagement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "post_id",
            foreignKey = @ForeignKey(
                    name = "fk_post_engagement_post",
                    foreignKeyDefinition = "FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE"
            )
    )
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(
                    name = "fk_post_engagement_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
            )
    )
    private User user;

    public PostEngagement() {}

    public PostEngagement(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    public PostEngagement(Long id, Post post, User user) {
        this.id = id;
        this.post = post;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
