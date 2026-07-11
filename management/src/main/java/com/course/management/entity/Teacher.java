package com.course.management.entity;

import lombok.Data;

@Data
public class Teacher {
    private Long id;
    private Long userId;
    private String teacherId;
    private String department;
    private String title;
    private String office;

    // 关联查询字段
    private String username;
    private String realName;
    private String email;
}