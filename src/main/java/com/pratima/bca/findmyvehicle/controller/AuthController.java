package com.pratima.bca.findmyvehicle.controller;

import com.pratima.bca.findmyvehicle.dto.*;
import com.pratima.bca.findmyvehicle.dto.*;
import com.pratima.bca.findmyvehicle.entity.User;
import com.pratima.bca.findmyvehicle.service.AuthService;
import com.pratima.bca.findmyvehicle.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid RegisterRequest registerRequest){
        Status status = new Status();
        Response response = new Response();
        if (userService.existsByEmail(registerRequest.getEmail())) {
            status.setStatus(409);
            status.setMessage("User already Exist.");
            response.setStatus(status);
            return ResponseEntity.ok(response);
        }

        try{
           User user = userService.registerUser(registerRequest);
            status.setStatus(200);
            status.setMessage("User has been registerd sucessfully.");
            response.setStatus(status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            status.setStatus(500);
            status.setMessage("Internal Server Error.");
            response.setStatus(status);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody @Valid LoginRequest loginRequest){
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-password")
    public ResponseEntity<Response> changePassword(
            @Valid @RequestBody ChangePasswordDto changePasswordDto) {

        Response response =
                authService.changePassword(changePasswordDto);
        return ResponseEntity.ok(response);
    }
}
