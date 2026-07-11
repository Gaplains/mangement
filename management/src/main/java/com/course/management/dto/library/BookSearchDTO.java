package com.course.management.dto.library;

import lombok.Data;

@Data
public class BookSearchDTO {
    private String keyword;
    private Long categoryId;
    private String status;
}
