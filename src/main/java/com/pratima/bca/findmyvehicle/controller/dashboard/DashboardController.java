package com.pratima.bca.findmyvehicle.controller.dashboard;

import com.pratima.bca.findmyvehicle.dto.Response;
import com.pratima.bca.findmyvehicle.dto.Status;
import com.pratima.bca.findmyvehicle.dto.dashboard.DashboardData;
import com.pratima.bca.findmyvehicle.service.dashboard.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<Response<DashboardData>> getDashboard() {
        Response<DashboardData> response = new Response<>();
        response.setStatus(Status.builder()
                .status(HttpStatus.OK.value())
                .message("Dashboard data retrieved.")
                .build());
        response.setData(dashboardService.getDashboardData());
        return ResponseEntity.ok(response);
    }
}
