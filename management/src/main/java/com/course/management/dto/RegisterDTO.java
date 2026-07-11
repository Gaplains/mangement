package com.course.management.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private String realName;
    private String studentId;
}