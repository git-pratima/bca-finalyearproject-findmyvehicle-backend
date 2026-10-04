package com.pratima.bca.findmyvehicle.dto.vehicle;

import com.pratima.bca.findmyvehicle.enums.VehicleCompany;
import com.pratima.bca.findmyvehicle.enums.VehicleStatus;
import com.pratima.bca.findmyvehicle.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDetailsDto {
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
    private boolean ownVehicle;
    private boolean found;
    private List<String> imageUrls;
    private List<MissingDetailsDetailsDto> missingDetails;
}
