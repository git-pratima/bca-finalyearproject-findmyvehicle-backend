package com.pratima.bca.findmyvehicle.dto.vehicle;

import com.pratima.bca.findmyvehicle.enums.Country;
import com.pratima.bca.findmyvehicle.enums.State;
import com.pratima.bca.findmyvehicle.enums.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissingDetailsDetailsDto {
    private Long id;
    private LocalDate missingDate;
    private LocalTime missingTime;
    private LocalDate foundDate;
    private Country country;
    private State state;
    private String district;
    private String city;
    private String pinCode;
    private String missingAddress;
    private String description;
    private VehicleStatus vehicleStatus;
    private String reward;
    private boolean ownReport;
    private boolean found;
}
