package com.course.management.dto.library;

import lombok.Data;

@Data
public class BorrowApplyDTO {
    private Long userId;
    private Long bookId;
    private String remark;
}
