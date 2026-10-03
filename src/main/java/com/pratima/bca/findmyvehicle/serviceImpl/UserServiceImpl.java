package com.pratima.bca.findmyvehicle.serviceImpl;

import com.pratima.bca.findmyvehicle.dto.*;
import com.pratima.bca.findmyvehicle.dto.*;
import com.pratima.bca.findmyvehicle.entity.User;
import com.pratima.bca.findmyvehicle.enums.AuthProvider;
import com.pratima.bca.findmyvehicle.enums.UserRole;
import com.pratima.bca.findmyvehicle.exception.InvalidCredentialsException;
import com.pratima.bca.findmyvehicle.exception.ResourceNotFoundException;
import com.pratima.bca.findmyvehicle.repository.UserRepository;
import com.pratima.bca.findmyvehicle.service.UserService;
import com.pratima.bca.findmyvehicle.util.JwtUtils;
import com.pratima.bca.findmyvehicle.util.MapperService;
import com.pratima.bca.findmyvehicle.util.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final ImageService imageService;

    @Autowired
    private MapperService mapperService;

    @Override
    public User registerUser(RegisterRequest registerRequest) {

        UserRole role = UserRole.NORMAL;

        if (registerRequest.getRole() != null) {
            role=registerRequest.getRole();
        }
        User userToSave = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNumber(registerRequest.getPhoneNumber())
                .role(role)
                .build();

       User user = userRepository.save(userToSave);

        return user;
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {

       User user = userRepository.findByEmail(loginRequest.getEmail())
               .orElseThrow(()-> new ResourceNotFoundException("Email not Found."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("password does not match");
        }
        Boolean emailVarified = user.getEmailVerified();
        Status status = new Status();
        UserIdentity identity = new UserIdentity();
        if(true){
            String token = jwtUtils.generateToken(user.getEmail());
            identity.setToken(token);
            identity.setUserName(user.getName());
            identity.setRole(user.getRole());
            identity.setUserId(user.getId());
            identity.setEmail(user.getEmail());
            status.setStatus(200);
            status.setMessage("User logged in successfully.");
        }else{
            status.setStatus(403);
            status.setMessage("Email not verified. Please verify your email before logging in..");
        }

        return Response.builder().userIdentity(identity)
                .status(status).build();
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByEmailAndId(String email, Long id) {
        return null;
    }

    @Override
    public UserDetails findOrCreateSocialUser(String email, String name, String provider) {

        // Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            log.info("Existing social user found: {}", email);
            return existingUser.get();
        }

        log.info("Creating new social user: email={}, provider={}", email, provider);

        // First time social login → create new user
        User newUser = User.builder()
                .email(email)
                .name(name)
                .password(null)                              // ✅ no password for social users
                .phoneNumber(null)                           // ✅ no phone for social users
                .role(UserRole.NORMAL)                      // ✅ use UserRole enum (default role)
                .provider(AuthProvider.valueOf(provider.toUpperCase()))  // ✅ "google" → AuthProvider.GOOGLE
                .emailVerified(true)                         // ✅ Google emails are pre-verified
                .build();

        return userRepository.save(newUser);
    }

    @Override
    public UserProfile getUserProfile(Long id) {
        Optional<User> user = userRepository.findById(id);
        UserProfile userProfile = null;
        if(user.isPresent()){
            userProfile = mapperService.userToUserProfile(user.get());
        }

        return userProfile;
    }

    @Override
    public User createUserProfile(User user, MultipartFile imageFile) {
        user = userRepository.save(user);

        if (imageFile != null && !imageFile.isEmpty()) {
            String profileImageUrl = imageService.uploadProfileImage(user.getId(), imageFile);
            user.setProfilePic(profileImageUrl);
            user = userRepository.save(user);
        }

        return user;
    }

    @Override
    public User findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not Found."));
        return user;
    }
}
