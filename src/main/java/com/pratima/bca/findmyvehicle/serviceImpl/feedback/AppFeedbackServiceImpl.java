package com.pratima.bca.findmyvehicle.serviceImpl.feedback;

import com.pratima.bca.findmyvehicle.dto.feedback.AppFeedbackDetailsDto;
import com.pratima.bca.findmyvehicle.dto.feedback.CreateAppFeedbackRequest;
import com.pratima.bca.findmyvehicle.entity.feedback.AppFeedback;
import com.pratima.bca.findmyvehicle.exception.ResourceNotFoundException;
import com.pratima.bca.findmyvehicle.repository.feedback.AppFeedbackRepository;
import com.pratima.bca.findmyvehicle.service.feedback.AppFeedbackService;
import com.pratima.bca.findmyvehicle.util.MultiFunctionUtility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppFeedbackServiceImpl implements AppFeedbackService {

    @Autowired
    private AppFeedbackRepository appFeedbackRepository;

    @Autowired
    private MultiFunctionUtility multiFunctionUtility;

    @Override
    @Transactional
    public AppFeedbackDetailsDto createAppFeedback(CreateAppFeedbackRequest request) {
        String comment = request.getComment();
        AppFeedback appFeedback = AppFeedback.builder()
                .rating(request.getRating())
                .comments(comment == null || comment.isBlank() ? null : comment.trim())
                .seen("N")
                .feedbackGivenBy(multiFunctionUtility.getCurrentUser().getEmail())
                .build();

        AppFeedback saved = appFeedbackRepository.save(appFeedback);
        return toDetailsDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppFeedbackDetailsDto> getAppFeedback(int page, int size) {
        Sort latestFirst = Sort.by(Sort.Order.desc("createdDate"), Sort.Order.desc("id"));
        return appFeedbackRepository.findAll(PageRequest.of(page, size, latestFirst))
                .map(this::toDetailsDto);
    }

    @Override
    @Transactional
    public AppFeedbackDetailsDto toggleSeen(Long appFeedbackId) {
        AppFeedback appFeedback = appFeedbackRepository.findById(appFeedbackId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application feedback not found with id: " + appFeedbackId));

        boolean isSeen = "Y".equalsIgnoreCase(appFeedback.getSeen());
        appFeedback.setSeen(isSeen ? "N" : "Y");
        appFeedback.setSeenBy(isSeen ? null : multiFunctionUtility.getCurrentUser().getEmail());
        return toDetailsDto(appFeedbackRepository.save(appFeedback));
    }

    private AppFeedbackDetailsDto toDetailsDto(AppFeedback appFeedback) {
        return AppFeedbackDetailsDto.builder()
                .id(appFeedback.getId())
                .rating(appFeedback.getRating())
                .comment(appFeedback.getComments())
                .seen(appFeedback.getSeen())
                .seenBy(appFeedback.getSeenBy())
                .feedbackGivenBy(appFeedback.getFeedbackGivenBy())
                .createdDate(appFeedback.getCreatedDate())
                .build();
    }
}
