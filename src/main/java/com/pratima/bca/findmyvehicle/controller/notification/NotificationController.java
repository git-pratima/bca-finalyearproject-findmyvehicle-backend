package com.pratima.bca.findmyvehicle.controller.notification;

import com.pratima.bca.findmyvehicle.dto.Response;
import com.pratima.bca.findmyvehicle.dto.Status;
import com.pratima.bca.findmyvehicle.dto.notification.CreateNotificationRequest;
import com.pratima.bca.findmyvehicle.dto.notification.NotificationDetailsDto;
import com.pratima.bca.findmyvehicle.service.notification.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/notifications")
    public ResponseEntity<Response<Page<NotificationDetailsDto>>> getCurrentUserNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 0 || size < 1 || size > 100) {
            Response<Page<NotificationDetailsDto>> response = new Response<>();
            response.setStatus(Status.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Page must be non-negative and size must be between 1 and 100.")
                    .build());
            return ResponseEntity.badRequest().body(response);
        }

        Response<Page<NotificationDetailsDto>> response = new Response<>();
        Status status = new Status();
        status.setStatus(HttpStatus.OK.value());
        status.setMessage("Notifications retrieved.");
        response.setStatus(status);
        response.setData(notificationService.getNotificationsForCurrentUser(page, size));
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/notifications/{notificationId}/seen")
    public ResponseEntity<Response<NotificationDetailsDto>> updateSeenStatus(
            @PathVariable Long notificationId,
            @RequestParam String seen) {
        if (!"Y".equals(seen) && !"N".equals(seen)) {
            Response<NotificationDetailsDto> response = new Response<>();
            response.setStatus(Status.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("seen must be Y or N.")
                    .build());
            return ResponseEntity.badRequest().body(response);
        }

        Response<NotificationDetailsDto> response = new Response<>();
        response.setStatus(Status.builder()
                .status(HttpStatus.OK.value())
                .message("Notification seen status updated.")
                .build());
        response.setData(notificationService.updateSeenStatusForCurrentUser(notificationId, seen));
        return ResponseEntity.ok(response);
    }
}
