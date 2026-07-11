package com.course.management.entity;

import lombok.Data;
import java.util.Date;

@Data
public class BookCategory {
    private Long id;
    private String name;
    private String description;
    private Integer sortOrder;
    private Date createdAt;
}
