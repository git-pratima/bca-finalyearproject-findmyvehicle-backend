package com.pratima.bca.findmyvehicle.controller.feedback;

import com.pratima.bca.findmyvehicle.dto.Response;
import com.pratima.bca.findmyvehicle.dto.Status;
import com.pratima.bca.findmyvehicle.dto.feedback.CreateFeedbackRequest;
import com.pratima.bca.findmyvehicle.dto.feedback.FeedbackDetailsDto;
import com.pratima.bca.findmyvehicle.service.feedback.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

}
