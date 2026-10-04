package com.pratima.bca.findmyvehicle.dto.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CreateNotificationRequest {

    @NotBlank(message = "Registration number is required.")
    private String regNo;

    @NotNull(message = "Missing details id is required.")
    private Long missingDetailsId;

    @NotNull(message = "Seen-at value is required.")
    private Date seenAt;

    @NotBlank(message = "Seen area is required.")
    private String seenArea;

    private String liveMapLink;

    private String message;

    @Pattern(regexp = "[YN]", message = "seenByVehicleOwner must be Y or N.")
    private String seenByVehicleOwner = "N";
}
