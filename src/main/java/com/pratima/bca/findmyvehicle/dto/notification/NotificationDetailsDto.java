package com.pratima.bca.findmyvehicle.dto.notification;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class NotificationDetailsDto {
    private Long id;
    private Long vehicleId;
    private String regNo;
    private Long missingDetailsId;
    private Long notifiedByUserId;
    private Long vehicleOwnerUserId;
    private Date seenAt;
    private String seenArea;
    private String liveMapLink;
    private String message;
    private String seenByVehicleOwner;
}
