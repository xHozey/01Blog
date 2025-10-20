package com._Blog.Backend.model;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "report_post")
public class ReportPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", foreignKey = @ForeignKey(name = "fk_report_post_reporter_id", foreignKeyDefinition = "FOREIGN KEY (reporter_id) REFERENCES users(id) ON DELETE CASCADE"))
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id", foreignKey = @ForeignKey(name = "fk_report_post_user_id", foreignKeyDefinition = "FOREIGN KEY (reported_user_id) REFERENCES users(id) ON DELETE CASCADE"))
    private User reportedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_post_id", foreignKey = @ForeignKey(name = "fk_report_post_user_post_id", foreignKeyDefinition = "FOREIGN KEY (reported_post_id) REFERENCES post(id) ON DELETE CASCADE"))
    private Post reportedPost;
    
    @Size(max=250)
    private String description;

    @CreationTimestamp
    private Timestamp createAt;

    public ReportPost(User reporter, User reportedUser, Post post, String description) {
        this.reporter = reporter;
        this.reportedUser = reportedUser;
        this.description = description;
        this.reportedPost = post;
    }

}
