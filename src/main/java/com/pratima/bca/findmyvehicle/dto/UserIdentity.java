package com.pratima.bca.findmyvehicle.dto;

import com.pratima.bca.findmyvehicle.enums.UserRole;
import lombok.Data;

@Data
public class UserIdentity {
    private Long userId;
    private String email;
    private String token;
    private UserRole role;
    private String userName;
}
