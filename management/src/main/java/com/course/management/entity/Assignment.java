package com.course.management.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Assignment {
    private Long id;
    private Long courseId;
    private String title;
    private String description;
    private Double maxScore;
    private Date deadline;
    private Date createdAt;
}