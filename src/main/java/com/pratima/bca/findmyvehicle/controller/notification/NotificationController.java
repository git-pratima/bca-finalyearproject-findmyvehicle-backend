package com.pratima.bca.findmyvehicle.controller.notification;

import com.pratima.bca.findmyvehicle.dto.Response;
import com.pratima.bca.findmyvehicle.dto.Status;
import com.pratima.bca.findmyvehicle.dto.notification.CreateNotificationRequest;
import com.pratima.bca.findmyvehicle.dto.notification.NotificationDetailsDto;
import com.pratima.bca.findmyvehicle.service.notification.NotificationService;
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
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/notifications")
    public ResponseEntity<Response<NotificationDetailsDto>> createNotification(
            @Valid @RequestBody CreateNotificationRequest request) {
        Response<NotificationDetailsDto> response = new Response<>();
        Status status = new Status();
        status.setStatus(HttpStatus.CREATED.value());
        status.setMessage("Notification created.");
        response.setStatus(status);
        response.setData(notificationService.createNotification(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
