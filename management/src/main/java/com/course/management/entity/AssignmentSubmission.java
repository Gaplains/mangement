package com.course.management.entity;

import lombok.Data;
import java.util.Date;

@Data
public class AssignmentSubmission {
    private Long id;
    private Long assignmentId;
    private Long studentId;
    private String submissionText;
    private String attachmentPath;
    private Date submittedAt;
    private Double score;
    private String feedback;
    private String status; // SUBMITTED, GRADED, LATE

    // 关联查询字段
    private String studentName;
    private String assignmentTitle;
}