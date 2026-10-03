package com.pratima.bca.findmyvehicle.controller;

import com.pratima.bca.findmyvehicle.dto.Response;
import com.pratima.bca.findmyvehicle.dto.Status;
import com.pratima.bca.findmyvehicle.dto.UserProfile;
import com.pratima.bca.findmyvehicle.entity.User;
import com.pratima.bca.findmyvehicle.exception.ResourceNotFoundException;
import com.pratima.bca.findmyvehicle.service.UserService;
import com.pratima.bca.findmyvehicle.util.MailService;
import com.pratima.bca.findmyvehicle.util.MapperService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private MapperService mapperService;

    @Autowired
    private MailService mailService;

    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserProfile(@PathVariable Long id) {
        UserProfile data = userService.getUserProfile(id);
        Status status = new Status();
        status.setStatus(HttpStatus.OK.value());
        status.setMessage("User Profile retrieved.");

        Response response = new Response();
        response.setStatus(status);
        response.setData(data);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> updateUserProfile(@RequestPart("userProfile") @Valid UserProfile userProfile,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        User user = userService.findById(userProfile.getId());
        if (user==null) {
            throw new ResourceNotFoundException("User Not Exist.");
        }

        user = mapperService.userProfileToUser(userProfile, user);

        user = userService.createUserProfile(user,imageFile);

        Status status = new Status();
        status.setStatus(HttpStatus.CREATED.value());
        status.setMessage("Profile has been updated.");

        Response response = new Response();
        response.setStatus(status);

        mailService.sendEmail(
                user.getEmail(),
                "FindMyVehicle - Your Profile Has Been Updated",
                "Hello " + user.getName() + ",\n\n"
                        + "Your FindMyVehicle profile has been successfully updated.\n\n"
                        + "If you made this change, no further action is required.\n\n"
                        + "If you did not make this change, please log in to your FindMyVehicle account "
                        + "and secure your account immediately.\n"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
