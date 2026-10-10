package com.pratima.bca.findmyvehicle.service.feedback;

import com.pratima.bca.findmyvehicle.dto.feedback.CreateFeedbackRequest;
import com.pratima.bca.findmyvehicle.dto.feedback.FeedbackDetailsDto;

public interface FeedbackService {

    FeedbackDetailsDto createFeedback(CreateFeedbackRequest request);
}
