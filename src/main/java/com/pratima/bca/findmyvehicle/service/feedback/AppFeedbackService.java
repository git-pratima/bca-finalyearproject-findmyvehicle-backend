package com.pratima.bca.findmyvehicle.service.feedback;

import com.pratima.bca.findmyvehicle.dto.feedback.AppFeedbackDetailsDto;
import com.pratima.bca.findmyvehicle.dto.feedback.CreateAppFeedbackRequest;
import org.springframework.data.domain.Page;

public interface AppFeedbackService {

    AppFeedbackDetailsDto createAppFeedback(CreateAppFeedbackRequest request);

    Page<AppFeedbackDetailsDto> getAppFeedback(int page, int size);

    AppFeedbackDetailsDto toggleSeen(Long appFeedbackId);
}
