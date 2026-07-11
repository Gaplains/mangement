package com.course.management.entity;

import lombok.Data;

@Data
public class Student {
    private Long id;
    private Long userId;
    private String studentId;
    private String className;
    private String major;
    private Integer enrollmentYear;

    // 关联查询字段
    private String username;
    private String realName;
    private String email;
}