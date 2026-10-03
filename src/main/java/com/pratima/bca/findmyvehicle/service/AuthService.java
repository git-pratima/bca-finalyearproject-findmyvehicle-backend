package com.pratima.bca.findmyvehicle.service;

import com.pratima.bca.findmyvehicle.dto.ChangePasswordDto;
import com.pratima.bca.findmyvehicle.dto.Response;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    Response changePassword(ChangePasswordDto changePasswordDto);

}
