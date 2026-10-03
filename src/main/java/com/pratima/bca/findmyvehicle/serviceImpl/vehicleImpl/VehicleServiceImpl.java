package com.pratima.bca.findmyvehicle.serviceImpl.vehicleImpl;

import com.pratima.bca.findmyvehicle.dto.vehicle.VehicleDto;
import com.pratima.bca.findmyvehicle.dto.vehicle.VehicleDetailsDto;
import com.pratima.bca.findmyvehicle.dto.vehicle.MissingDetailsDetailsDto;
import com.pratima.bca.findmyvehicle.entity.vehicle.MissingDetails;
import com.pratima.bca.findmyvehicle.entity.vehicle.Vehicle;
import com.pratima.bca.findmyvehicle.entity.vehicle.VehicleImage;
import com.pratima.bca.findmyvehicle.enums.VehicleStatus;
import com.pratima.bca.findmyvehicle.exception.ResourceNotFoundException;
import com.pratima.bca.findmyvehicle.repository.vehicle.VehicleRepository;
import com.pratima.bca.findmyvehicle.service.vehicle.VehicleService;
import com.pratima.bca.findmyvehicle.util.ImageService;
import com.pratima.bca.findmyvehicle.util.MultiFunctionUtility;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired 
    private ImageService imageService;

    @Autowired
    private MultiFunctionUtility multiFunctionUtility;

    @Override
    public Boolean existsByRegNumber(String regNumber) {
        return vehicleRepository.existsByRegNumber(regNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleDetailsDto getVehicleDetails(String regNumber) {
        Vehicle vehicle = vehicleRepository.findByRegNumberIgnoreCase(regNumber.trim())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle not found for registration number: " + regNumber));

        return toVehicleDetailsDto(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleDetailsDto> getVehiclesReportedByCurrentUser(
            String regNumber, String model, String city, String pinCode, int page, int size) {
        validatePage(page, size);

        Long userId = multiFunctionUtility.getCurrentUser().getId();
        Specification<Vehicle> specification = buildVehicleSearchSpecification(
                userId, regNumber, model, city, pinCode, null);

        return vehicleRepository.findAll(specification, PageRequest.of(
                        page, size, Sort.by(Sort.Direction.DESC, "createdDate")))
                .map(this::toVehicleDetailsDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleDetailsDto> getAllVehiclesReported(
            String regNumber, String model, String city, String pinCode,
            VehicleStatus status, int page, int size) {
        validatePage(page, size);
        Specification<Vehicle> specification = buildVehicleSearchSpecification(
                null, regNumber, model, city, pinCode, status);

        return vehicleRepository.findAll(specification, PageRequest.of(
                        page, size, Sort.by(Sort.Direction.DESC, "createdDate")))
                .map(this::toVehicleDetailsDto);
    }

    private Specification<Vehicle> buildVehicleSearchSpecification(
            Long reportedById, String regNumber, String model, String city,
            String pinCode, VehicleStatus status) {
        String registrationFilter = normalizeSearchValue(regNumber);
        String modelFilter = normalizeSearchValue(model);
        String cityFilter = normalizeSearchValue(city);
        String pinCodeFilter = normalizeSearchValue(pinCode);

        return (root, query, criteriaBuilder) -> {
            Join<Vehicle, MissingDetails> missingDetails = root.join("missingDetails", JoinType.LEFT);
            List<Predicate> predicates = new ArrayList<>();
            if (reportedById != null) {
                predicates.add(criteriaBuilder.equal(root.get("reportedBy").get("id"), reportedById));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(missingDetails.get("vehicleStatus"), status));
            }
            addContainsPredicate(predicates, criteriaBuilder, root.get("regNumber"), registrationFilter);
            addContainsPredicate(predicates, criteriaBuilder, root.get("vehicleModel"), modelFilter);
            addContainsPredicate(predicates, criteriaBuilder, missingDetails.get("city"), cityFilter);
            addContainsPredicate(predicates, criteriaBuilder, missingDetails.get("pinCode"), pinCodeFilter);
            query.distinct(true);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleDetailsDto> searchMissingVehicles(
            String regNumber, String model, String city, String pinCode, int page, int size) {
        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException("Page must be non-negative and size must be between 1 and 100.");
        }

        String registrationFilter = normalizeSearchValue(regNumber);
        String modelFilter = normalizeSearchValue(model);
        String cityFilter = normalizeSearchValue(city);
        String pinCodeFilter = normalizeSearchValue(pinCode);

        Specification<Vehicle> specification = (root, query, criteriaBuilder) -> {
            Join<Vehicle, MissingDetails> missingDetails = root.join("missingDetails");
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(
                    missingDetails.get("vehicleStatus"), VehicleStatus.MISSING));
            addContainsPredicate(predicates, criteriaBuilder, root.get("regNumber"), registrationFilter);
            addContainsPredicate(predicates, criteriaBuilder, root.get("vehicleModel"), modelFilter);
            addContainsPredicate(predicates, criteriaBuilder, missingDetails.get("city"), cityFilter);
            addContainsPredicate(predicates, criteriaBuilder, missingDetails.get("pinCode"), pinCodeFilter);
            query.distinct(true);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return vehicleRepository.findAll(specification, PageRequest.of(page, size))
                .map(this::toVehicleDetailsDto);
    }

    private void addContainsPredicate(
            List<Predicate> predicates,
            jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
            jakarta.persistence.criteria.Expression<String> field,
            String filter) {
        if (filter != null) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(field),
                    "%" + filter.toLowerCase(Locale.ROOT) + "%"));
        }
    }

    private String normalizeSearchValue(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private void validatePage(int page, int size) {
        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException("Page must be non-negative and size must be between 1 and 100.");
        }
    }

    private VehicleDetailsDto toVehicleDetailsDto(Vehicle vehicle) {
        return VehicleDetailsDto.builder()
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
                        .map(VehicleImage::getImageUrl)
                        .toList())
                .missingDetails(vehicle.getMissingDetails().stream()
                        .map(details -> MissingDetailsDetailsDto.builder()
                                .id(details.getId())
                                .missingDate(details.getMissingDate())
                                .missingTime(details.getMissingTime())
                                .foundDate(details.getFoundDate())
                                .country(details.getCountry())
                                .state(details.getState())
                                .district(details.getDistrict())
                                .city(details.getCity())
                                .pinCode(details.getPinCode())
                                .missingAddress(details.getMissingAddress())
                                .description(details.getDescription())
                                .vehicleStatus(details.getVehicleStatus())
                                .reward(details.getReward())
                                .build())
                        .toList())
                .build();
    }

    @Override
    public List<Vehicle> getRecentMissingVehicles(Integer count) {
        if (count == null || count <= 0) {
            return List.of();
        }
        return vehicleRepository.findDistinctByMissingDetails_VehicleStatusOrderByCreatedDateDesc(
                VehicleStatus.MISSING, PageRequest.of(0, count));
    }

    @Override
    @Transactional
    public Vehicle reportMissingVehicle(VehicleDto vehicleDto, List<MultipartFile> imageFile) {
        String regNumber = vehicleDto.getRegNumber().trim();
        Vehicle vehicle = vehicleRepository.findByRegNumberIgnoreCase(regNumber)
                .orElseGet(() -> {
                    Vehicle newVehicle = mapper.map(vehicleDto, Vehicle.class);
                    newVehicle.setRegNumber(regNumber);
                    newVehicle.setReportedBy(multiFunctionUtility.getCurrentUser());
                    newVehicle.setMissingDetails(new ArrayList<>());
                    newVehicle.setImages(new ArrayList<>());
                    return newVehicle;
                });

        MissingDetails missingDetails =
                mapper.map(vehicleDto.getMissingDetails(), MissingDetails.class);
        missingDetails.setVehicleStatus(VehicleStatus.MISSING);
        missingDetails.setVehicle(vehicle);

        if (vehicle.getMissingDetails() == null) {
            vehicle.setMissingDetails(new ArrayList<>());
        }
        vehicle.getMissingDetails().add(missingDetails);
        vehicle.setVehicleStatus(VehicleStatus.MISSING);

        List<String> urls = imageService.uploadVehicleImages(vehicle.getRegNumber(), imageFile);
        if (vehicle.getImages() == null) {
            vehicle.setImages(new ArrayList<>());
        }
        for (String url : urls) {
            VehicleImage vehicleImage = new VehicleImage();
            vehicleImage.setVehicle(vehicle);
            vehicleImage.setImageUrl(url);
            vehicle.getImages().add(vehicleImage);
        }

        return vehicleRepository.save(vehicle);
    }
}
