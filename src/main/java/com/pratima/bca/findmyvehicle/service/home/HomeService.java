package com.pratima.bca.findmyvehicle.service.home;

import com.pratima.bca.findmyvehicle.dto.home.DashboardData;
import org.springframework.stereotype.Service;

@Service
public interface HomeService {

    void refreshHomeDashboardData();

    DashboardData getDashboardData();

}
