package com.pratima.bca.findmyvehicle.dto.feedback;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateAppFeedbackRequest {

    @NotNull(message = "Rating is required.")
    @Min(value = 1, message = "Rating must be between 1 and 5.")
    @Max(value = 5, message = "Rating must be between 1 and 5.")
    private Integer rating;

    @Size(max = 3000, message = "Comment must not exceed 3000 characters.")
    private String comment;
}
