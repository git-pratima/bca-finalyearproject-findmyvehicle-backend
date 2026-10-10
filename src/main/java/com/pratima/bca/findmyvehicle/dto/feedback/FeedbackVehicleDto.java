package com.pratima.bca.findmyvehicle.dto.feedback;

import com.pratima.bca.findmyvehicle.enums.VehicleCompany;
import com.pratima.bca.findmyvehicle.enums.VehicleStatus;
import com.pratima.bca.findmyvehicle.enums.VehicleType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FeedbackVehicleDto {
    private Long id;
    private String regNumber;
    private String chassisNumber;
    private String engineNumber;
    private String owner;
    private String ownerEmail;
    private String ownerMobile;
    private String color;
    private VehicleType type;
    private VehicleCompany vehicleCompany;
    private VehicleStatus vehicleStatus;
    private String vehicleModel;
    private List<String> imageUrls;
}
