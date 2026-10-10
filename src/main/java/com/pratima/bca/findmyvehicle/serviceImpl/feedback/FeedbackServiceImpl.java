package com.pratima.bca.findmyvehicle.serviceImpl.feedback;

import com.pratima.bca.findmyvehicle.dto.feedback.CreateFeedbackRequest;
import com.pratima.bca.findmyvehicle.dto.feedback.FeedbackDashboardDto;
import com.pratima.bca.findmyvehicle.dto.feedback.FeedbackDetailsDto;
import com.pratima.bca.findmyvehicle.dto.feedback.FeedbackMissingReportDto;
import com.pratima.bca.findmyvehicle.dto.feedback.FeedbackVehicleDto;
import com.pratima.bca.findmyvehicle.entity.feedback.Feedback;
import com.pratima.bca.findmyvehicle.entity.vehicle.MissingDetails;
import com.pratima.bca.findmyvehicle.entity.vehicle.Vehicle;
import com.pratima.bca.findmyvehicle.exception.ResourceNotFoundException;
import com.pratima.bca.findmyvehicle.repository.feedback.FeedbackRepository;
import com.pratima.bca.findmyvehicle.repository.vehicle.MissingDetailsRepository;
import com.pratima.bca.findmyvehicle.service.feedback.FeedbackService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private MissingDetailsRepository missingDetailsRepository;

    @Override
    @Transactional
    public FeedbackDetailsDto createFeedback(CreateFeedbackRequest request) {
        MissingDetails missingDetails = missingDetailsRepository.findById(request.getMissingReportId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Missing report not found with id: " + request.getMissingReportId()));

        Feedback feedback = Feedback.builder()
                .missingDetails(missingDetails)
                .rating(request.getRating())
                .comments(request.getComment().trim())
                .build();
        missingDetails.getFeedbacks().add(feedback);
        Feedback saved = feedbackRepository.save(feedback);

        return FeedbackDetailsDto.builder()
                .id(saved.getId())
                .missingReportId(missingDetails.getId())
                .rating(saved.getRating())
                .comment(saved.getComments())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackDashboardDto> getFeedback(int page, int size) {
        Sort latestFirst = Sort.by(Sort.Order.desc("createdDate"), Sort.Order.desc("id"));
        return feedbackRepository.findAll(PageRequest.of(page, size, latestFirst))
                .map(this::toFeedbackDashboardDto);
    }

    private FeedbackDashboardDto toFeedbackDashboardDto(Feedback feedback) {
        MissingDetails missingDetails = feedback.getMissingDetails();
        Vehicle vehicle = missingDetails.getVehicle();

        return FeedbackDashboardDto.builder()
                .id(feedback.getId())
                .rating(feedback.getRating())
                .comment(feedback.getComments())
                .createdDate(feedback.getCreatedDate())
                .missingReport(FeedbackMissingReportDto.builder()
                        .id(missingDetails.getId())
                        .missingDate(missingDetails.getMissingDate())
                        .missingTime(missingDetails.getMissingTime())
                        .foundDate(missingDetails.getFoundDate())
                        .country(missingDetails.getCountry())
                        .state(missingDetails.getState())
                        .district(missingDetails.getDistrict())
                        .city(missingDetails.getCity())
                        .pinCode(missingDetails.getPinCode())
                        .missingAddress(missingDetails.getMissingAddress())
                        .description(missingDetails.getDescription())
                        .vehicleStatus(missingDetails.getVehicleStatus())
                        .reward(missingDetails.getReward())
                        .build())
                .vehicle(FeedbackVehicleDto.builder()
                        .id(vehicle.getId())
                        .regNumber(vehicle.getRegNumber())
                        .chassisNumber(vehicle.getChassisNumber())
                        .engineNumber(vehicle.getEngineNumber())
                        .owner(vehicle.getOwner())
                        .ownerEmail(vehicle.getOwnerEmail())
                        .ownerMobile(vehicle.getOwnerMobile())
                        .color(vehicle.getColor())
                        .type(vehicle.getType())
                        .vehicleCompany(vehicle.getVehicleCompany())
                        .vehicleStatus(vehicle.getVehicleStatus())
                        .vehicleModel(vehicle.getVehicleModel())
                        .imageUrls(vehicle.getImages().stream()
                                .map(image -> image.getImageUrl())
                                .toList())
                        .build())
                .build();
    }
}
