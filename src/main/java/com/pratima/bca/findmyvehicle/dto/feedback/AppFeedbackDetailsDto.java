package com.pratima.bca.findmyvehicle.dto.feedback;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class AppFeedbackDetailsDto {
    private Long id;
    private Integer rating;
    private String comment;
    private String seen;
    private String seenBy;
    private String feedbackGivenBy;
    private Date createdDate;
}
