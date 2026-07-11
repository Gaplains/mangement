package com.course.management.entity;

import lombok.Data;
import java.util.Date;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String role; // ADMIN, TEACHER, STUDENT
    private String email;
    private String phone;
    private String realName;
    private String status; // ACTIVE, INACTIVE
    private Date createdAt;
    private Date updatedAt;

    // 前端需要的额外字段
    private String studentId;
    private String college;
    private String major;
    private String avatar;
}