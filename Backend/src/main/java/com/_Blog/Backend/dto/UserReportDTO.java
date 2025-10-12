package com._Blog.Backend.dto;

import java.sql.Timestamp;

import com._Blog.Backend.model.ReportUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReportDTO {
    private Long id;
    private String reporterName;
    private Long reporterId;
    private String reportedName;
    private Long reportedId;
    private String description;
    private Timestamp createAt;

    public UserReportDTO(ReportUser reportUser) {
        this.id = reportUser.getId();
        this.reporterName = reportUser.getReporter().getUsername();
        this.reporterId = reportUser.getReporter().getId();
        this.reportedName = reportUser.getReportedUser().getUsername();
        this.reportedId = reportUser.getReportedUser().getId();
        this.description = reportUser.getDescription();
        this.createAt = reportUser.getCreateAt();
    }
}
