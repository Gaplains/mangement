package com.course.management.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Book {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private Long categoryId;
    private String publisher;
    private Integer publishYear;
    private String description;
    private Integer totalStock;
    private Integer availableStock;
    private String location;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String categoryName;
}
