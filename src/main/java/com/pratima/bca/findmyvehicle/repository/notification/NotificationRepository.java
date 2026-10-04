package com.pratima.bca.findmyvehicle.repository.notification;

import com.pratima.bca.findmyvehicle.entity.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    long countByVehicleOwner_IdAndSeenByVehicleOwner(Long vehicleOwnerId, String seenByVehicleOwner);
}
