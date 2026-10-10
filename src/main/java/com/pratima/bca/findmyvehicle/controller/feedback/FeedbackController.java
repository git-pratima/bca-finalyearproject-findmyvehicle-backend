package com.pratima.bca.findmyvehicle.controller.feedback;

import com.pratima.bca.findmyvehicle.dto.Response;
import com.pratima.bca.findmyvehicle.dto.Status;
import com.pratima.bca.findmyvehicle.dto.feedback.CreateFeedbackRequest;
import com.pratima.bca.findmyvehicle.dto.feedback.FeedbackDashboardDto;
import com.pratima.bca.findmyvehicle.dto.feedback.FeedbackDetailsDto;
import com.pratima.bca.findmyvehicle.service.feedback.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/feedbacks")
    public ResponseEntity<Response<FeedbackDetailsDto>> createFeedback(
            @Valid @RequestBody CreateFeedbackRequest request) {
        Response<FeedbackDetailsDto> response = new Response<>();
        response.setStatus(Status.builder()
                .status(HttpStatus.CREATED.value())
                .message("Feedback submitted.")
                .build());
        response.setData(feedbackService.createFeedback(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<Response<Page<FeedbackDashboardDto>>> getFeedback(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 0 || size < 1 || size > 100) {
            Response<Page<FeedbackDashboardDto>> response = new Response<>();
            response.setStatus(Status.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Page must be non-negative and size must be between 1 and 100.")
                    .build());
            return ResponseEntity.badRequest().body(response);
        }

        Response<Page<FeedbackDashboardDto>> response = new Response<>();
        response.setStatus(Status.builder()
                .status(HttpStatus.OK.value())
                .message("Feedback retrieved.")
                .build());
        response.setData(feedbackService.getFeedback(page, size));
        return ResponseEntity.ok(response);
    }

}
