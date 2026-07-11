package com.course.management.entity;

import lombok.Data;
import java.util.Date;

@Data
public class CourseSelection {
    private Long id;
    private Long studentId;
    private Long courseId;
    private String status;
    private Double finalScore;
    private Date selectedAt;
    private Date createdAt;
    private Date updatedAt;

    // 关联字段
    private String studentName;
    private String courseName;
    private String teacherName;
}