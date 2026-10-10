package com.pratima.bca.findmyvehicle.serviceImpl.feedback;

import com.pratima.bca.findmyvehicle.dto.feedback.CreateFeedbackRequest;
import com.pratima.bca.findmyvehicle.dto.feedback.FeedbackDetailsDto;
import com.pratima.bca.findmyvehicle.entity.feedback.Feedback;
import com.pratima.bca.findmyvehicle.entity.vehicle.MissingDetails;
import com.pratima.bca.findmyvehicle.exception.ResourceNotFoundException;
import com.pratima.bca.findmyvehicle.repository.feedback.FeedbackRepository;
import com.pratima.bca.findmyvehicle.repository.vehicle.MissingDetailsRepository;
import com.pratima.bca.findmyvehicle.service.feedback.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private MissingDetailsRepository missingDetailsRepository;

    @Override
    @Transactional
    public FeedbackDetailsDto createFeedback(CreateFeedbackRequest request) {
        MissingDetails missingDetails = missingDetailsRepository.findById(request.getMissingReportId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Missing report not found with id: " + request.getMissingReportId()));

        Feedback feedback = Feedback.builder()
                .missingDetails(missingDetails)
                .rating(request.getRating())
                .comments(request.getComment().trim())
                .build();
        missingDetails.getFeedbacks().add(feedback);
        Feedback saved = feedbackRepository.save(feedback);

        return FeedbackDetailsDto.builder()
                .id(saved.getId())
                .missingReportId(missingDetails.getId())
                .rating(saved.getRating())
                .comment(saved.getComments())
                .build();
    }
}
