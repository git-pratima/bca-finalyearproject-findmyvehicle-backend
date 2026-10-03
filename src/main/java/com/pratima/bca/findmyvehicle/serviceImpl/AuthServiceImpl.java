package com.pratima.bca.findmyvehicle.serviceImpl;

import com.pratima.bca.findmyvehicle.exception.InvalidCredentialsException;
import com.pratima.bca.findmyvehicle.service.AuthService;
import com.pratima.bca.findmyvehicle.dto.Response;
import com.pratima.bca.findmyvehicle.dto.Status;
import com.pratima.bca.findmyvehicle.dto.ChangePasswordDto;
import com.pratima.bca.findmyvehicle.entity.User;
import com.pratima.bca.findmyvehicle.repository.UserRepository;
import com.pratima.bca.findmyvehicle.util.MailService;
import com.pratima.bca.findmyvehicle.util.MultiFunctionUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MultiFunctionUtility multiFunctionUtility;

    @Autowired
    private MailService mailService;

    @Override
    @Transactional
    public Response changePassword(ChangePasswordDto changePasswordDto) {

        // 1. Get currently logged-in user
        User user = multiFunctionUtility.getCurrentUser();

        if (user == null) {
            throw new IllegalStateException(
                    "Authenticated user could not be found."
            );
        }

        // 2. Google/social users may not have a local password
        if (user.getPassword() == null) {
            throw new InvalidCredentialsException(
                    "This account does not have a password. Please use the password setup option."
            );
        }

        // 3. Verify current password
        boolean currentPasswordMatches =
                passwordEncoder.matches(
                        changePasswordDto.getCurrentPassword(),
                        user.getPassword()
                );

        if (!currentPasswordMatches) {
            throw new InvalidCredentialsException(
                    "Current password is incorrect."
            );
        }

        // 4. Make sure new password and confirm password match
        if (!changePasswordDto.getNewPassword()
                .equals(changePasswordDto.getConfirmPassword())) {

            throw new InvalidCredentialsException(
                    "New password and confirm password do not match."
            );
        }

        // 5. Don't allow same password
        if (passwordEncoder.matches(
                changePasswordDto.getNewPassword(),
                user.getPassword())) {

            throw new InvalidCredentialsException(
                    "New password must be different from current password."
            );
        }

        // 6. Encode new password
        String encodedPassword =
                passwordEncoder.encode(
                        changePasswordDto.getNewPassword()
                );

        // 7. Update password
        user.setPassword(encodedPassword);

        // 8. Save user
        userRepository.save(user);

        // 9. Return response
        Status status = new Status();
        status.setStatus(200);
        status.setMessage("Password changed successfully.");

        Response response = new Response();
        response.setStatus(status);

        mailService.sendEmail(
                user.getEmail(),
                "FindMyVehicle - Password Reset OTP",
                "Your OTP is 123456. This OTP is valid for 10 minutes."
        );
        return response;
    }

}
