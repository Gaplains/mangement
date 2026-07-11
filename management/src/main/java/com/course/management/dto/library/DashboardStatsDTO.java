package com.course.management.dto.library;

import lombok.Data;

@Data
public class DashboardStatsDTO {
    private long bookCount;
    private long categoryCount;
    private long readerCount;
    private long totalStock;
    private long availableStock;
    private long borrowedStock;
    private long borrowRecords;
    private long pendingRecords;
    private long borrowedRecords;
    private long returnedRecords;
}
