package com.pratima.bca.findmyvehicle.repository.notification;

import com.pratima.bca.findmyvehicle.entity.notification.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    long countByVehicleOwner_IdAndSeenByVehicleOwner(Long vehicleOwnerId, String seenByVehicleOwner);

    Page<Notification> findByVehicleOwner_IdOrderByCreatedDateDesc(Long vehicleOwnerId, Pageable pageable);

    Optional<Notification> findByIdAndVehicleOwner_Id(Long id, Long vehicleOwnerId);
}
