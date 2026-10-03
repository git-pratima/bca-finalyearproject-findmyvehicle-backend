package com.pratima.bca.findmyvehicle.controller.home;

import com.pratima.bca.findmyvehicle.dto.Response;
import com.pratima.bca.findmyvehicle.dto.Status;
import com.pratima.bca.findmyvehicle.dto.home.DashboardData;
import com.pratima.bca.findmyvehicle.service.home.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/home", "/api/home"})
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/dashboard-data")
    public ResponseEntity<Response> reportMissingVehicle() {
        DashboardData data = homeService.getDashboardData();
        Status status = new Status();
        status.setStatus(HttpStatus.OK.value());
        status.setMessage("Home Dashboard Data retrieved.");

        Response response = new Response();
        response.setStatus(status);
        response.setData(data);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/refresh")
    public ResponseEntity<Response> refreshHomeDashboardData() {
        // Call the service to refresh dashboard data
        homeService.refreshHomeDashboardData();

        // Prepare status object
        Status status = new Status();
        status.setStatus(HttpStatus.OK.value());
        status.setMessage("Home Dashboard Data refreshed successfully.");

        // Build response
        Response response = new Response();
        response.setStatus(status);

        // Return response entity
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
