package com._Blog.Backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_engagement")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostEngagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_post_engagement_post", foreignKeyDefinition = "FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE"))
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_post_engagement_user", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"))
    private User user;

    public PostEngagement(Post post, User user) {
        this.post = post;
        this.user = user;
    }

}
