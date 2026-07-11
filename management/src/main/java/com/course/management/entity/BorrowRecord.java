package com.course.management.entity;

import lombok.Data;
import java.util.Date;

@Data
public class BorrowRecord {
    private Long id;
    private Long userId;
    private Long bookId;
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;
    private String status;
    private String remark;
    private Date createdAt;
    private String username;
    private String realName;
    private String bookTitle;
    private String isbn;
}
