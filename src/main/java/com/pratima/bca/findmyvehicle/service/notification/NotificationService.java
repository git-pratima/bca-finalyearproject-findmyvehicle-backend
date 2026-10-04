package com.pratima.bca.findmyvehicle.service.notification;

import com.pratima.bca.findmyvehicle.dto.notification.CreateNotificationRequest;
import com.pratima.bca.findmyvehicle.dto.notification.NotificationDetailsDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {

    NotificationDetailsDto createNotification(CreateNotificationRequest request);

    Page<NotificationDetailsDto> getNotificationsForCurrentUser(int page, int size);

    long getUnreadNotificationCountForCurrentUser();
}
