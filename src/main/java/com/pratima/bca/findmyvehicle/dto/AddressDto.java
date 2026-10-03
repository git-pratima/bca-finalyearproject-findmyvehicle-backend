package com.pratima.bca.findmyvehicle.dto;

import com.pratima.bca.findmyvehicle.enums.State;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor   // generates public Status(int, String)
@NoArgsConstructor
public class AddressDto {

    private Long id;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private State state;

    private String pinCode;

    private String country;
}
