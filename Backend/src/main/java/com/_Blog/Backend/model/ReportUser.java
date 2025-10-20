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

@Entity
@Table(name = "report_user")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReportUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", foreignKey = @ForeignKey(name = "fk_report_user_reporter_id", foreignKeyDefinition = "FOREIGN KEY (reporter_id) REFERENCES users(id) ON DELETE CASCADE"))
    private User reporter;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id", foreignKey = @ForeignKey(name = "fk_report_user_reported_id", foreignKeyDefinition = "FOREIGN KEY (reported_user_id) REFERENCES users(id) ON DELETE CASCADE"))
    private User reportedUser;

    @Size(max = 250)
    private String description;
    @CreationTimestamp
    private Timestamp createAt;

    public ReportUser(User reporter, User reportedUser, String description) {
        this.reporter = reporter;
        this.reportedUser = reportedUser;
        this.description = description;
    }

}
