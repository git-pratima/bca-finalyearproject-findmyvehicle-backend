package com.pratima.bca.findmyvehicle.serviceImpl.dashboard;

import com.pratima.bca.findmyvehicle.dto.dashboard.DashboardData;
import com.pratima.bca.findmyvehicle.entity.User;
import com.pratima.bca.findmyvehicle.entity.vehicle.MissingDetails;
import com.pratima.bca.findmyvehicle.entity.vehicle.Vehicle;
import com.pratima.bca.findmyvehicle.entity.vehicle.VehicleImage;
import com.pratima.bca.findmyvehicle.enums.VehicleStatus;
import com.pratima.bca.findmyvehicle.repository.vehicle.VehicleRepository;
import com.pratima.bca.findmyvehicle.service.dashboard.DashboardService;
import com.pratima.bca.findmyvehicle.util.MultiFunctionUtility;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final VehicleRepository vehicleRepository;
    private final MultiFunctionUtility multiFunctionUtility;
    private final int recentMissingVehiclesLimit;

    public DashboardServiceImpl(VehicleRepository vehicleRepository,
                                MultiFunctionUtility multiFunctionUtility,
                                @Value("${app.dashboard.recent-missing-vehicles-limit}") int recentMissingVehiclesLimit) {
        this.vehicleRepository = vehicleRepository;
        this.multiFunctionUtility = multiFunctionUtility;
        this.recentMissingVehiclesLimit = recentMissingVehiclesLimit;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardData getDashboardData() {
        User user = multiFunctionUtility.getCurrentUser();
        Long userId = user.getId();
        DashboardData.DashboardSummary summary = new DashboardData.DashboardSummary(
                vehicleRepository.countByReportedBy_Id(userId),
                vehicleRepository.countReportsByUserAndStatus(userId, VehicleStatus.FOUND),
                vehicleRepository.countReportsByUserAndStatus(userId, VehicleStatus.MISSING),
                vehicleRepository.countReportsByUserAndStatus(userId, VehicleStatus.CLOSED));

        List<DashboardData.DashboardVehicle> recentVehicles = vehicleRepository
                .findDistinctByMissingDetails_VehicleStatusOrderByCreatedDateDesc(
                        VehicleStatus.MISSING, PageRequest.of(0, recentMissingVehiclesLimit))
                .stream()
                .map(this::toDashboardVehicle)
                .toList();

        return new DashboardData(
                new DashboardData.DashboardUser(
                        user.getId(), user.getName(), user.getEmail(), user.getProfilePic()),
                summary,
                new DashboardData.DashboardActivity(null, null),
                recentVehicles);
    }

    private DashboardData.DashboardVehicle toDashboardVehicle(Vehicle vehicle) {
        MissingDetails latestDetails = vehicle.getMissingDetails().stream()
                .max(Comparator.comparing(MissingDetails::getId))
                .orElse(null);

        String location = null;
        String reportedAt = null;
        if (latestDetails != null) {
            String city = latestDetails.getCity();
            String state = latestDetails.getState() == null
                    ? null : latestDetails.getState().getDisplayName();
            location = city == null ? state : state == null ? city : city + ", " + state;
        }
        if (vehicle.getCreatedDate() != null) {
            reportedAt = vehicle.getCreatedDate().toInstant().toString();
        }

        String image = vehicle.getImages().stream()
                .map(VehicleImage::getImageUrl)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        return new DashboardData.DashboardVehicle(
                vehicle.getId(),
                vehicle.getVehicleModel(),
                vehicle.getRegNumber(),
                location,
                reportedAt,
                image,
                vehicle.getChassisNumber(),
                vehicle.getEngineNumber(),
                vehicle.getColor(),
                latestDetails == null ? null : latestDetails.getDescription(),
                latestDetails == null || latestDetails.getVehicleStatus() == null
                        ? null : latestDetails.getVehicleStatus().name());
    }
}