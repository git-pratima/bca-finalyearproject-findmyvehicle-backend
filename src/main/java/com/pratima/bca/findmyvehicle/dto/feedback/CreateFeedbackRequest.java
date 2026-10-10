package com.pratima.bca.findmyvehicle.dto.feedback;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateFeedbackRequest {

    @NotNull(message = "Missing report id is required.")
    private Long missingReportId;

    @NotNull(message = "Rating is required.")
    private Integer rating;

    @NotBlank(message = "Comment is required.")
    @Size(max = 3000, message = "Comment must not exceed 3000 characters.")
    private String comment;
}
