package com._Blog.Backend.dto;

import java.sql.Timestamp;

import com._Blog.Backend.model.ReportPost;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostReportDTO {
    private Long id;
    private String reporterName;
    private Long reporterId;
    private String reportedName;
    private Long reportedId;
    private String description;
    private Long postId;
    private Timestamp createAt;

    public PostReportDTO(ReportPost reportPost) {
        this.id = reportPost.getId();
        this.reporterName = reportPost.getReporter().getUsername();
        this.reporterId = reportPost.getReporter().getId();
        this.reportedName = reportPost.getReportedUser().getUsername();
        this.reportedId = reportPost.getReportedUser().getId();
        this.description = reportPost.getDescription();
        this.createAt = reportPost.getCreateAt();
        this.postId = reportPost.getReportedPost().getId();
    }
}
