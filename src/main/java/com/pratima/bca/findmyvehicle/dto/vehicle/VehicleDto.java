package com.pratima.bca.findmyvehicle.dto.vehicle;

import com.pratima.bca.findmyvehicle.enums.VehicleCompany;
import com.pratima.bca.findmyvehicle.enums.VehicleStatus;
import com.pratima.bca.findmyvehicle.enums.VehicleType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDto {

        @NotBlank(message = "Registration number is required.")
        private String regNumber;

        @NotBlank(message = "Chassis number is required.")
        private String chassisNumber;

        @NotBlank(message = "Provide the vehicle owner.")
        private String owner;

        @NotBlank(message = "Provide the vehicle owner.")
        private String ownerMobile;

        @NotBlank(message = "Provide the vehicle owner.")
        private String ownerEmail;

        private String color;

        @NotNull(message = "Vehicle type is required.")
        private VehicleType type;

        @NotNull(message = "Vehicle company is required.")
        private VehicleCompany vehicleCompany;

        private VehicleStatus vehicleStatus;

        private String vehicleModel;

        private String engineNumber;

        @Valid
        @NotNull(message = "Missing details are required.")
        private MissingDetailsDto missingDetails;

}
