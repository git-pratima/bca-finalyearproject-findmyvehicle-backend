package com.pratima.bca.findmyvehicle.serviceImpl.notification;

import com.pratima.bca.findmyvehicle.dto.notification.CreateNotificationRequest;
import com.pratima.bca.findmyvehicle.dto.notification.NotificationDetailsDto;
import com.pratima.bca.findmyvehicle.entity.notification.Notification;
import com.pratima.bca.findmyvehicle.entity.User;
import com.pratima.bca.findmyvehicle.entity.vehicle.MissingDetails;
import com.pratima.bca.findmyvehicle.entity.vehicle.Vehicle;
import com.pratima.bca.findmyvehicle.exception.ResourceNotFoundException;
import com.pratima.bca.findmyvehicle.repository.notification.NotificationRepository;
import com.pratima.bca.findmyvehicle.repository.vehicle.MissingDetailsRepository;
import com.pratima.bca.findmyvehicle.repository.vehicle.VehicleRepository;
import com.pratima.bca.findmyvehicle.service.notification.NotificationService;
import com.pratima.bca.findmyvehicle.util.MultiFunctionUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private MissingDetailsRepository missingDetailsRepository;

    @Autowired
    private MultiFunctionUtility multiFunctionUtility;

    @Override
    @Transactional
    public NotificationDetailsDto createNotification(CreateNotificationRequest request) {
        String regNo = request.getRegNo().trim();
        Vehicle vehicle = vehicleRepository.findByRegNumberIgnoreCase(regNo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle not found with registration number: " + regNo));

        MissingDetails missingDetails = missingDetailsRepository.findById(request.getMissingDetailsId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Missing report not found with id: " + request.getMissingDetailsId()));

        if (!missingDetails.getVehicle().getId().equals(vehicle.getId())) {
            throw new ResourceNotFoundException(
                    "Missing report id " + missingDetails.getId()
                            + " is not associated with vehicle id: " + vehicle.getId());
        }

        User currentUser = multiFunctionUtility.getCurrentUser();
        String seenByVehicleOwner = request.getSeenByVehicleOwner();
        Notification notification = Notification.builder()
                .vehicle(vehicle)
                .missingDetails(missingDetails)
                .notifiedBy(currentUser)
                .vehicleOwner(vehicle.getReportedBy())
                .seenAt(request.getSeenAt())
                .seenArea(request.getSeenArea())
                .liveMapLink(request.getLiveMapLink())
                .message(request.getMessage())
                .seenByVehicleOwner(seenByVehicleOwner == null || seenByVehicleOwner.isBlank()
                        ? "N" : seenByVehicleOwner)
                .build();

        Notification saved = notificationRepository.save(notification);
        return NotificationDetailsDto.builder()
                .id(saved.getId())
                .vehicleId(vehicle.getId())
                .regNo(vehicle.getRegNumber())
                .missingDetailsId(missingDetails.getId())
                .notifiedByUserId(currentUser.getId())
                .vehicleOwnerUserId(vehicle.getReportedBy().getId())
                .seenAt(saved.getSeenAt())
                .seenArea(saved.getSeenArea())
                .liveMapLink(saved.getLiveMapLink())
                .message(saved.getMessage())
                .seenByVehicleOwner(saved.getSeenByVehicleOwner())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadNotificationCountForCurrentUser() {
        Long currentUserId = multiFunctionUtility.getCurrentUser().getId();
        return notificationRepository.countByVehicleOwner_IdAndSeenByVehicleOwner(currentUserId, "N");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDetailsDto> getNotificationsForCurrentUser(int page, int size) {
        Long currentUserId = multiFunctionUtility.getCurrentUser().getId();
        return notificationRepository.findByVehicleOwner_IdOrderByCreatedDateDesc(
                        currentUserId, PageRequest.of(page, size))
                .map(notification -> NotificationDetailsDto.builder()
                        .id(notification.getId())
                        .vehicleId(notification.getVehicle().getId())
                        .regNo(notification.getVehicle().getRegNumber())
                        .missingDetailsId(notification.getMissingDetails().getId())
                        .notifiedByUserId(notification.getNotifiedBy().getId())
                        .vehicleOwnerUserId(notification.getVehicleOwner().getId())
                        .seenAt(notification.getSeenAt())
                        .seenArea(notification.getSeenArea())
                        .liveMapLink(notification.getLiveMapLink())
                        .message(notification.getMessage())
                        .seenByVehicleOwner(notification.getSeenByVehicleOwner())
                        .build());
    }
}
