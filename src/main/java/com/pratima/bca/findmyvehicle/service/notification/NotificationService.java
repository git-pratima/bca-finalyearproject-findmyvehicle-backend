package com.pratima.bca.findmyvehicle.service.notification;

import com.pratima.bca.findmyvehicle.dto.notification.CreateNotificationRequest;
import com.pratima.bca.findmyvehicle.dto.notification.NotificationDetailsDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface NotificationService {

    NotificationDetailsDto createNotification(CreateNotificationRequest request);

    NotificationDetailsDto createNotification(CreateNotificationRequest request, MultipartFile imageFile);

    Page<NotificationDetailsDto> getNotificationsForCurrentUser(int page, int size);

    NotificationDetailsDto updateSeenStatusForCurrentUser(Long notificationId, String seen);

    long getUnreadNotificationCountForCurrentUser();
}
