package com.pratima.bca.findmyvehicle.service.vehicle;

import com.pratima.bca.findmyvehicle.dto.vehicle.VehicleDto;
import com.pratima.bca.findmyvehicle.dto.vehicle.VehicleDetailsDto;
import com.pratima.bca.findmyvehicle.entity.vehicle.Vehicle;
import com.pratima.bca.findmyvehicle.enums.VehicleStatus;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface VehicleService {

    Boolean existsByRegNumber(String regNumber);

    VehicleDetailsDto getVehicleDetails(String regNumber);

    VehicleDetailsDto getVehicleDetails(String regNumber, Long missingDetailsId);

    VehicleDetailsDto markMissingReportAsFound(Long missingDetailsId);

    Page<VehicleDetailsDto> getVehiclesReportedByCurrentUser(
            String regNumber, String model, String city, String pinCode, int page, int size);

    Page<VehicleDetailsDto> getAllVehiclesReported(
            String regNumber, String model, String city, String pinCode,
            VehicleStatus status, int page, int size);

    Page<VehicleDetailsDto> searchMissingVehicles(
            String regNumber, String model, String city, String pinCode, int page, int size);

    Vehicle reportMissingVehicle(VehicleDto vehicleDto, List<MultipartFile> imageFile);

    List<Vehicle> getRecentMissingVehicles(Integer count);

}
