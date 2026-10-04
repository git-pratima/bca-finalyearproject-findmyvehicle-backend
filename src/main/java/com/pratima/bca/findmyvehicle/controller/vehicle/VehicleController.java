package com.pratima.bca.findmyvehicle.controller.vehicle;

import com.pratima.bca.findmyvehicle.dto.Response;
import com.pratima.bca.findmyvehicle.dto.Status;
import com.pratima.bca.findmyvehicle.dto.vehicle.VehicleDetailsDto;
import com.pratima.bca.findmyvehicle.dto.vehicle.VehicleDto;
import com.pratima.bca.findmyvehicle.enums.VehicleStatus;
import com.pratima.bca.findmyvehicle.service.vehicle.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/vehicle/{regNumber}")
    public ResponseEntity<Response<VehicleDetailsDto>> getVehicleDetails(
            @PathVariable String regNumber) {
        Response<VehicleDetailsDto> response = new Response<>();
        Status status = new Status();
        status.setStatus(HttpStatus.OK.value());
        status.setMessage("Vehicle details retrieved.");
        response.setStatus(status);
        response.setData(vehicleService.getVehicleDetails(regNumber));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vehicles/{regNo}/missing-details/{missingDetailsId}")
    public ResponseEntity<Response<VehicleDetailsDto>> getVehicleAndMissingDetails(
            @PathVariable String regNo,
            @PathVariable Long missingDetailsId) {
        Response<VehicleDetailsDto> response = new Response<>();
        Status status = new Status();
        status.setStatus(HttpStatus.OK.value());
        status.setMessage("Vehicle and missing report retrieved.");
        response.setStatus(status);
        response.setData(vehicleService.getVehicleDetails(regNo, missingDetailsId));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    @PutMapping("/missing-details/{missingDetailsId}/found")
    public ResponseEntity<Response<VehicleDetailsDto>> markMissingReportAsFound(
            @PathVariable Long missingDetailsId) {
        Response<VehicleDetailsDto> response = new Response<>();
        Status status = new Status();
        status.setStatus(HttpStatus.OK.value());
        status.setMessage("Vehicle and missing report marked as found.");
        response.setStatus(status);
        response.setData(vehicleService.markMissingReportAsFound(missingDetailsId));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    @GetMapping("/vehicles/reported-by-me")
    public ResponseEntity<Response<Page<VehicleDetailsDto>>> getVehiclesReportedByCurrentUser(
            @RequestParam(required = false) String regNumber,
            @RequestParam(required = false) String model,
            @RequestParam(name = "missingCity", required = false) String city,
            @RequestParam(required = false) String pinCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 0 || size < 1 || size > 100) {
            Status status = new Status();
            status.setStatus(HttpStatus.BAD_REQUEST.value());
            status.setMessage("Page must be non-negative and size must be between 1 and 100.");
            Response<Page<VehicleDetailsDto>> response = new Response<>();
            response.setStatus(status);
            return ResponseEntity.badRequest().body(response);
        }

        Response<Page<VehicleDetailsDto>> response = new Response<>();
        Status status = new Status();
        status.setStatus(HttpStatus.OK.value());
        status.setMessage("Your reported vehicles retrieved.");
        response.setStatus(status);
        response.setData(vehicleService.getVehiclesReportedByCurrentUser(
                regNumber, model, city, pinCode, page, size));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    @GetMapping("/vehicles/reported-all")
    public ResponseEntity<Response<Page<VehicleDetailsDto>>> getAllVehiclesReported(
            @RequestParam(required = false) String regNumber,
            @RequestParam(required = false) String model,
            @RequestParam(name = "missingCity", required = false) String city,
            @RequestParam(required = false) String pinCode,
            @RequestParam(required = false) VehicleStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 0 || size < 1 || size > 100) {
            Status responseStatus = new Status();
            responseStatus.setStatus(HttpStatus.BAD_REQUEST.value());
            responseStatus.setMessage("Page must be non-negative and size must be between 1 and 100.");
            Response<Page<VehicleDetailsDto>> response = new Response<>();
            response.setStatus(responseStatus);
            return ResponseEntity.badRequest().body(response);
        }

        Response<Page<VehicleDetailsDto>> response = new Response<>();
        Status responseStatus = new Status();
        responseStatus.setStatus(HttpStatus.OK.value());
        responseStatus.setMessage("All reported vehicles retrieved.");
        response.setStatus(responseStatus);
        response.setData(vehicleService.getAllVehiclesReported(
                regNumber, model, city, pinCode, status, page, size));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vehicles/search")
    public ResponseEntity<Response<Page<VehicleDetailsDto>>> searchMissingVehicles(
            @RequestParam(required = false) String regNumber,
            @RequestParam(required = false) String model,
            @RequestParam(name = "missingCity", required = false) String city,
            @RequestParam(required = false) String pinCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 0 || size < 1 || size > 100) {
            Status status = new Status();
            status.setStatus(HttpStatus.BAD_REQUEST.value());
            status.setMessage("Page must be non-negative and size must be between 1 and 100.");
            Response<Page<VehicleDetailsDto>> response = new Response<>();
            response.setStatus(status);
            return ResponseEntity.badRequest().body(response);
        }

        Response<Page<VehicleDetailsDto>> response = new Response<>();
        Status status = new Status();
        status.setStatus(HttpStatus.OK.value());
        status.setMessage("Missing vehicles retrieved.");
        response.setStatus(status);
        response.setData(vehicleService.searchMissingVehicles(regNumber, model, city, pinCode, page, size));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    @PostMapping(value = "/reportMissingVehicle",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> reportMissingVehicle(@RequestPart("vehicle") @Valid VehicleDto vehicleDto,
            @RequestPart(value = "imageFile", required = false) List<MultipartFile> imageFile) {

        vehicleService.reportMissingVehicle(vehicleDto,imageFile);
        Status status = new Status();
        status.setStatus(HttpStatus.CREATED.value());
        status.setMessage("Missing report has been registered.");
        Response response = new Response();
        response.setStatus(status);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
