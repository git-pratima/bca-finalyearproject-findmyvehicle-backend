package com.pratima.bca.findmyvehicle.serviceImpl.notification;

import com.pratima.bca.findmyvehicle.dto.notification.CreateNotificationRequest;
import com.pratima.bca.findmyvehicle.dto.notification.NotificationDetailsDto;
import com.pratima.bca.findmyvehicle.entity.notification.Notification;
import com.pratima.bca.findmyvehicle.entity.User;
import com.pratima.bca.findmyvehicle.entity.vehicle.MissingDetails;
import com.pratima.bca.findmyvehicle.entity.vehicle.Vehicle;
import com.pratima.bca.findmyvehicle.entity.vehicle.VehicleImage;
import com.pratima.bca.findmyvehicle.exception.ResourceNotFoundException;
import com.pratima.bca.findmyvehicle.repository.notification.NotificationRepository;
import com.pratima.bca.findmyvehicle.repository.vehicle.MissingDetailsRepository;
import com.pratima.bca.findmyvehicle.repository.vehicle.VehicleRepository;
import com.pratima.bca.findmyvehicle.service.notification.NotificationService;
import com.pratima.bca.findmyvehicle.util.MultiFunctionUtility;
import com.pratima.bca.findmyvehicle.util.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

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

    @Autowired
    private ImageService imageService;

    @Override
    @Transactional
    public NotificationDetailsDto createNotification(CreateNotificationRequest request) {
        return createNotification(request, null);
    }

    @Override
    @Transactional
    public NotificationDetailsDto createNotification(CreateNotificationRequest request, MultipartFile imageFile) {
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
                .images(new ArrayList<>())
                .build();

        Notification saved = notificationRepository.save(notification);
        String imageUrl = imageService.uploadNotificationImage(regNo, saved.getId(), imageFile);
        if (imageUrl != null) {
            VehicleImage image = VehicleImage.builder()
                    .notification(saved)
                    .imageUrl(imageUrl)
                    .build();
            saved.getImages().add(image);
            saved = notificationRepository.save(saved);
        }
        return toNotificationDetailsDto(saved);
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
                .map(this::toNotificationDetailsDto);
    }

    @Override
    @Transactional
    public NotificationDetailsDto updateSeenStatusForCurrentUser(Long notificationId, String seen) {
        Long currentUserId = multiFunctionUtility.getCurrentUser().getId();
        Notification notification = notificationRepository
                .findByIdAndVehicleOwner_Id(notificationId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with id: " + notificationId));

        notification.setSeenByVehicleOwner(seen);
        return toNotificationDetailsDto(notificationRepository.save(notification));
    }

    private NotificationDetailsDto toNotificationDetailsDto(Notification notification) {
        return NotificationDetailsDto.builder()
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
                .imageUrl(notification.getImages().stream()
                        .findFirst()
                        .map(VehicleImage::getImageUrl)
                        .orElse(null))
                .build();
    }
}
