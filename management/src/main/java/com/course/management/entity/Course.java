package com.course.management.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Course {
    private Long id;
    private String courseCode;
    private String courseName;
    private Double credit;
    private String description;
    private Long teacherId;
    private Integer maxStudents;
    private Integer currentStudents;
    private String semester;
    private String status; // OPEN, CLOSED, CANCELLED
    private Date createdAt;

    // 关联查询字段
    private String teacherName;
}