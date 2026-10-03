package com.pratima.bca.findmyvehicle.dto.vehicle;

import com.pratima.bca.findmyvehicle.enums.State;
import com.pratima.bca.findmyvehicle.enums.Country;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissingDetailsDto {

    @Schema(
            type = "string",
            example = "2026-07-27",
            description = "Missing date in yyyy-MM-dd format"
    )
    @NotNull(message = "Missing date is required.")
    private LocalDate missingDate;

    @Schema(
            type = "string",
            example = "10:10:10",
            description = "Missing time in HH:mm:ss format"
    )
    @NotNull(message = "Missing time is required.")
    private LocalTime missingTime;

    @NotNull(message = "Country is required.")
    private Country country;

    @NotNull(message = "State is required.")
    private State state;

    @NotBlank(message = "District is required.")
    private String district;

    @NotBlank(message = "City is required.")
    private String city;

    @NotBlank(message = "PIN code is required.")
    @Pattern(
            regexp = "^[1-9][0-9]{5}$",
            message = "PIN code must be a valid 6-digit Indian PIN code."
    )
    private String pinCode;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters.")
    @NotBlank(message = "Address is required.")
    private String address;

    private String description;

    @Size(max = 200, message = "Reward cannot exceed 100 characters.")
    private String reward;
}
