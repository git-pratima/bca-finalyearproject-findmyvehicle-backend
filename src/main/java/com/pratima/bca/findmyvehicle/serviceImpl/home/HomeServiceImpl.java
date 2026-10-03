package com.pratima.bca.findmyvehicle.serviceImpl.home;

import com.pratima.bca.findmyvehicle.dto.home.DashboardData;
import com.pratima.bca.findmyvehicle.entity.home.HomeDashData;
import com.pratima.bca.findmyvehicle.entity.home.Statistics;
import com.pratima.bca.findmyvehicle.enums.VehicleStatus;
import com.pratima.bca.findmyvehicle.repository.UserRepository;
import com.pratima.bca.findmyvehicle.repository.home.HomeRepository;
import com.pratima.bca.findmyvehicle.repository.vehicle.MissingDetailsRepository;
import com.pratima.bca.findmyvehicle.repository.vehicle.VehicleRepository;
import com.pratima.bca.findmyvehicle.service.home.HomeService;
import com.pratima.bca.findmyvehicle.util.MapperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class HomeServiceImpl implements HomeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HomeRepository homeRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private MissingDetailsRepository missingDetailsRepository;

    @Autowired
    private MapperService mapperService;

    @Override
    @Transactional
    public void refreshHomeDashboardData() {
        log.info("Refreshing Home Dashboard Data...");
        HomeDashData homeDashDataDB = homeRepository.findFirstByOrderByIdAsc();
        HomeDashData homeDashData = homeDashDataDB == null ? new HomeDashData() : homeDashDataDB;
        if (homeDashDataDB == null) {
            log.warn("No existing dashboard record found. Creating a new one...");
        } else {
            log.debug("Existing dashboard record found with ID: {}", homeDashDataDB.getId());
        }

        homeDashData.setEyebrow("India's community recovery network");
        homeDashData.setTitle("Find Your Missing Vehicle Faster.");
        homeDashData.setHighlightedWord("Faster");
        homeDashData.setDescription("A community platform that connects vehicle owners, citizens and authorities to help recover missing or stolen vehicles.");
        homeDashData.setSearchPlaceholder("Search Reg. Number");
        homeDashData.setReportMissingUrl("/report");
        homeDashData.setSearchVehiclesUrl("/search");
        homeDashData.getStatistics().clear();
        homeDashData.getStatistics().addAll(createStatistics(homeDashData));
        homeRepository.save(homeDashData);
        log.info("Dashboard data updated successfully.");
    }

    @Override
    @Transactional
    public DashboardData getDashboardData() {

        HomeDashData homeDashDataDB = homeRepository.findFirstByOrderByIdAsc();
        if (homeDashDataDB == null) {
            refreshHomeDashboardData();
            homeDashDataDB = homeRepository.findFirstByOrderByIdAsc();
        }

        DashboardData dashboardData = mapperService.homeDashDataToDashboardDate(homeDashDataDB);
        dashboardData.setStatisticsDtos(
                mapperService.statisticsListToStatisticsDtoList(createStatistics(homeDashDataDB)));

        return dashboardData;
    }

    private List<Statistics> createStatistics(HomeDashData homeDashData) {
        return List.of(
                createStatistic(homeDashData, "vehiclesReported",
                        vehicleRepository.count(),
                        "Vehicles Reported", "Across India", "directions_car"),
                createStatistic(homeDashData, "vehiclesRecovered",
                        vehicleRepository.countVehiclesByMissingDetailsStatus(VehicleStatus.FOUND),
                        "Vehicles Recovered", "Successfully Recovered", "verified_user"),
                createStatistic(homeDashData, "registeredUsers", userRepository.count(),
                        "Registered Users", "Trusted Community", "group"),
                createStatistic(homeDashData, "statesCovered",
                        missingDetailsRepository.countDistinctReportedStates(),
                        "States Covered", "Across India", "location_on"));
    }

    private Statistics createStatistic(
            HomeDashData homeDashData, String key, long value,
            String label, String description, String icon) {
        return Statistics.builder()
                .key(key)
                .value(Long.toString(value))
                .label(label)
                .description(description)
                .icon(icon)
                .homeDashData(homeDashData)
                .build();
    }
}
