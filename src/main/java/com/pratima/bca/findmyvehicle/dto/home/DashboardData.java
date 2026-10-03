package com.pratima.bca.findmyvehicle.dto.home;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardData {

    @Builder.Default
    private HomeHeader header = new HomeHeader();

    private List<StatisticsDto> statisticsDtos = new ArrayList<StatisticsDto>();

    @Builder.Default
    private RecentMissingVehicles recentMissingVehicles = new RecentMissingVehicles();
}

