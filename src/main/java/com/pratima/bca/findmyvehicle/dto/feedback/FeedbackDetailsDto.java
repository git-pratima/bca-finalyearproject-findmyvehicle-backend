package com.pratima.bca.findmyvehicle.dto.feedback;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedbackDetailsDto {
    private Long id;
    private Long missingReportId;
    private Integer rating;
    private String comment;
}
