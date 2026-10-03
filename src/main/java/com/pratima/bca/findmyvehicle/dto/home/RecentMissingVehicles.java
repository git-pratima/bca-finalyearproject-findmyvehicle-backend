package com.pratima.bca.findmyvehicle.dto.home;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentMissingVehicles {

    private String title;

    private String viewAllUrl;

    private Long totalCount;

    private List<VehicleDetails> items;

}
