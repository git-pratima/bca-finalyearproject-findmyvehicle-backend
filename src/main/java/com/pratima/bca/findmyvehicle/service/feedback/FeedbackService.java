package com.pratima.bca.findmyvehicle.service.feedback;

import com.pratima.bca.findmyvehicle.dto.feedback.CreateFeedbackRequest;
import com.pratima.bca.findmyvehicle.dto.feedback.FeedbackDashboardDto;
import com.pratima.bca.findmyvehicle.dto.feedback.FeedbackDetailsDto;
import org.springframework.data.domain.Page;

public interface FeedbackService {

    FeedbackDetailsDto createFeedback(CreateFeedbackRequest request);

    Page<FeedbackDashboardDto> getFeedback(int page, int size);
}
