package com.pratima.bca.findmyvehicle.dto.feedback;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class FeedbackDashboardDto {
    private Long id;
    private Integer rating;
    private String comment;
    private Date createdDate;
    private FeedbackMissingReportDto missingReport;
    private FeedbackVehicleDto vehicle;
}
