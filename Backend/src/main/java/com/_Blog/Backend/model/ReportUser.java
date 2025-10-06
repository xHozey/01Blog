package com._Blog.Backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table("user_reports")
public class ReportUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;

    private String description;
    @CreationTimestamp
    private Timestamp date;

    public ReportUser(Long id,  User reporter, User reportedUser, String description) {
        this.id = id;
        this.reporter = reporter;
        this.reportedUser = reportedUser;
        this.description = description;
    }

    public ReportUser(User reporter, User reportedUser, String description) {
        this.reporter = reporter;
        this.reportedUser = reportedUser;
        this.description = description;
    }


    public ReportUser() {}

    public User getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
