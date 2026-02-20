package com.abhi.cabin_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardSummaryDTO {

    private long totalInterviews;
    private long scheduledCount;
    private long waitingCount;
    private long completedCount;
    private long totalStudents;
    private long totalCompanies;
}
