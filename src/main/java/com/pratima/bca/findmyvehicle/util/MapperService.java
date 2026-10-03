package com.pratima.bca.findmyvehicle.util;

import com.pratima.bca.findmyvehicle.dto.AddressDto;
import com.pratima.bca.findmyvehicle.dto.UserProfile;
import com.pratima.bca.findmyvehicle.dto.home.DashboardData;
import com.pratima.bca.findmyvehicle.dto.home.Location;
import com.pratima.bca.findmyvehicle.dto.home.RecentMissingVehicles;
import com.pratima.bca.findmyvehicle.dto.home.Reward;
import com.pratima.bca.findmyvehicle.dto.home.StatisticsDto;
import com.pratima.bca.findmyvehicle.dto.home.VehicleDetails;
import com.pratima.bca.findmyvehicle.entity.Address;
import com.pratima.bca.findmyvehicle.entity.User;
import com.pratima.bca.findmyvehicle.entity.home.HomeDashData;
import com.pratima.bca.findmyvehicle.repository.UserRepository;
import com.pratima.bca.findmyvehicle.service.vehicle.VehicleService;
import com.pratima.bca.findmyvehicle.entity.home.Statistics;
import com.pratima.bca.findmyvehicle.entity.vehicle.MissingDetails;
import com.pratima.bca.findmyvehicle.entity.vehicle.Vehicle;
import com.pratima.bca.findmyvehicle.entity.vehicle.VehicleImage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapperService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleService vehicleService;

    public DashboardData homeDashDataToDashboardDate(HomeDashData homeDashData){
        DashboardData dashboardData = new DashboardData();

        //header
        dashboardData.getHeader().setEyebrow(homeDashData.getEyebrow());
        dashboardData.getHeader().setTitle(homeDashData.getTitle());
        dashboardData.getHeader().setHighlightedWord(homeDashData.getHighlightedWord());
        dashboardData.getHeader().setDescription(homeDashData.getDescription());
        dashboardData.getHeader().setSearchPlaceholder(homeDashData.getSearchPlaceholder());
        dashboardData.getHeader().setReportMissingUrl(homeDashData.getReportMissingUrl());
        dashboardData.getHeader().setSearchVehiclesUrl(homeDashData.getSearchVehiclesUrl());

        //Statistics
        List<Statistics> statisticsList = homeDashData.getStatistics();
        List<StatisticsDto> statisticsDtoList = statisticsListToStatisticsDtoList(statisticsList);
        dashboardData.setStatisticsDtos(statisticsDtoList);

        //Get recent 5 missing vehicles reported
        List<Vehicle> fourMissingVehicles = vehicleService.getRecentMissingVehicles(4);

        List<VehicleDetails> vehicleDetailsList = new ArrayList<>();
        RecentMissingVehicles recentMissingVehicles = new RecentMissingVehicles();

        if(fourMissingVehicles!=null && !fourMissingVehicles.isEmpty()){
            vehicleDetailsList = vehicleListToVehicleDetailsList(fourMissingVehicles);

            recentMissingVehicles.setTitle("Recently Missing Vehicles.");
            recentMissingVehicles.setTotalCount(Long.valueOf(fourMissingVehicles.size()));
            recentMissingVehicles.setViewAllUrl("/search");
            recentMissingVehicles.setItems(vehicleDetailsList);
            dashboardData.setRecentMissingVehicles(recentMissingVehicles);
        }
        return dashboardData;
    }

    private List<VehicleDetails> vehicleListToVehicleDetailsList(List<Vehicle> fourMissingVehicles) {
        if(fourMissingVehicles==null || fourMissingVehicles.isEmpty()){
            return List.of();
        }else{
            List<VehicleDetails> vehicleDetailsList = new ArrayList<>();

            for(Vehicle vehicle : fourMissingVehicles){
                VehicleDetails vehicleDetails = new VehicleDetails();
                vehicleDetails.setId(vehicle.getId() != null ? vehicle.getId().toString() : null);
                vehicleDetails.setRegistrationNumber(vehicle.getRegNumber());
                vehicleDetails.setBrand(vehicle.getVehicleCompany());
                vehicleDetails.setModel(vehicle.getVehicleModel());
                vehicleDetails.setVehicleType(vehicle.getType());
                vehicleDetails.setDisplayName(vehicle.getVehicleModel());
                vehicleDetails.setStatus(vehicle.getVehicleStatus());
                vehicleDetails.setDetailUrl("/search?q=" + vehicle.getRegNumber());
                
                //imageUrl is set to null by default, can be set later if needed
                //image
                List<VehicleImage> images = vehicle.getImages();

                if(images!=null && !images.isEmpty()){
                    List<String> imageUrls = new ArrayList<>();
                    for(VehicleImage image : images){
                        imageUrls.add(image.getImageUrl());
                    }
                    vehicleDetails.setImageUrls(imageUrls);
                }

                List<MissingDetails> addresses = vehicle.getMissingDetails();
                if(addresses!=null && !addresses.isEmpty()){
                    MissingDetails missingDetails = addresses.stream().max(Comparator.comparing(MissingDetails::getId)).orElse(null);
                    if (missingDetails != null) {
                        String city = missingDetails.getCity();
                        String state = missingDetails.getState() != null ? missingDetails.getState().name() : null;
                        String displayName = city == null ? state : state == null ? city : city + " " + state;
                        Location location = Location.builder()
                                .city(city)
                                .state(missingDetails.getState())
                                .displayName(displayName)
                                .build();
                        vehicleDetails.setMissingLocation(location);
                        vehicleDetails.setMissigngDateTime(missingDetails.getMissingDate() != null
                                ? java.sql.Date.valueOf(missingDetails.getMissingDate()) : null);
                        if (vehicleDetails.getStatus() == null) {
                            vehicleDetails.setStatus(missingDetails.getVehicleStatus());
                        }
                        String rewardAmount = missingDetails.getReward();
                        if (rewardAmount != null && !rewardAmount.isBlank()) {
                            Reward reward = Reward.builder()
                                    .amount(Double.valueOf(rewardAmount))
                                    .currency("INR")
                                    .displayName("")
                                    .build();
                            vehicleDetails.setReward(reward);
                        }
                    }
                }

                vehicleDetailsList.add(vehicleDetails);
            }
            return vehicleDetailsList;
        }
    }


    public List<StatisticsDto> statisticsListToStatisticsDtoList(List<Statistics> statisticsList) {

        List<StatisticsDto> statisticsDtoList = new ArrayList<StatisticsDto>();
        if(statisticsList!=null && !statisticsList.isEmpty()){
            for(Statistics statistics : statisticsList){
                StatisticsDto statisticsDto = new StatisticsDto();
                statisticsDto.setKey(statistics.getKey());
                statisticsDto.setValue(Long.valueOf(statistics.getValue()));
                statisticsDto.setLabel(statistics.getLabel());
                statisticsDto.setDescription(statistics.getDescription());
                statisticsDto.setIcon(statistics.getIcon());
                statisticsDtoList.add(statisticsDto);
        }
        }
        return statisticsDtoList;
    }

    public UserProfile userToUserProfile(User user) {
        Address address = user.getAddress();
        UserProfile userProfile = new UserProfile();
        userProfile.setId(user.getId());
        userProfile.setName(user.getName());
        userProfile.setEmail(user.getEmail());
        userProfile.setPhone(user.getPhoneNumber());
        userProfile.setProfileImageUrl(user.getProfilePic());

        if(address!=null){
            userProfile.getAddress().setId(address.getId());
            userProfile.getAddress().setAddressLine1(address.getAddressLine1());
            userProfile.getAddress().setAddressLine2(address.getAddressLine2());
            userProfile.getAddress().setCity(address.getCity());
            userProfile.getAddress().setCountry(address.getCountry());
            userProfile.getAddress().setState(address.getState());
            userProfile.getAddress().setPinCode(address.getPinCode());
        }
        return userProfile;
    }

    public User userProfileToUser(UserProfile userProfile, User user) {
        Address address = user.getAddress();
        if(address==null){
            address = new Address();
        }
        if(userProfile.getEmail()!=null){
            user.setEmail(userProfile.getEmail());
        }
        if(userProfile.getName()!=null){
            user.setName(userProfile.getName());
        }
        if(userProfile.getPhone()!=null){
            user.setPhoneNumber(userProfile.getPhone());
        }
        AddressDto addressDto = userProfile.getAddress();

        if(addressDto.getAddressLine1()!=null && !addressDto.getAddressLine1().trim().isEmpty()){
            address.setAddressLine1(addressDto.getAddressLine1());
        }
        if(addressDto.getAddressLine2()!=null && !addressDto.getAddressLine2().trim().isEmpty()){
            address.setAddressLine2(addressDto.getAddressLine2());
        }
        if(addressDto.getCity()!=null && !addressDto.getCity().trim().isEmpty()){
            address.setCity(addressDto.getCity());
        }
        if(addressDto.getState()!=null){
            address.setState(addressDto.getState());
        }
        if(addressDto.getState()!=null){
            address.setState(addressDto.getState());
        }
        if(addressDto.getPinCode()!=null && !addressDto.getPinCode().trim().isEmpty()){
            address.setPinCode(addressDto.getPinCode());
        }
        if(addressDto.getCountry()!=null && !addressDto.getCountry().trim().isEmpty()){
            address.setCountry(addressDto.getCountry());
        }
        address.setUser(user);
        user.setAddress(address);
        return user;
    }
}
