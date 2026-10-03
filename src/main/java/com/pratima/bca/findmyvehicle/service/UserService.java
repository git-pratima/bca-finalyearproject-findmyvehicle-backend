package com.pratima.bca.findmyvehicle.service;

import com.pratima.bca.findmyvehicle.dto.LoginRequest;
import com.pratima.bca.findmyvehicle.dto.RegisterRequest;
import com.pratima.bca.findmyvehicle.dto.Response;
import com.pratima.bca.findmyvehicle.dto.UserProfile;
import com.pratima.bca.findmyvehicle.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User registerUser(RegisterRequest registerRequest);

    Response loginUser(LoginRequest loginRequest);

    Boolean existsByEmail(String email);

    Boolean existsByEmailAndId(String email, Long id);

    UserDetails findOrCreateSocialUser(String email, String name, String provider);

    UserProfile getUserProfile(Long id);

    User createUserProfile(User user, MultipartFile imageFile);

    User findById(Long id);
}
