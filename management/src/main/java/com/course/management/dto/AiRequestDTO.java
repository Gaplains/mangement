package com.course.management.dto;

import lombok.Data;

@Data
public class AiRequestDTO {
    private String question;
    private String keyword;
    private Long studentId;
    private String courseName;
    private String description;
}
