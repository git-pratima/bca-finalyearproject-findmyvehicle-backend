package com.pratima.bca.findmyvehicle.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor   // generates public Status(int, String)
@NoArgsConstructor
public class UserProfile {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private String profileImageUrl;

    @Builder.Default
    private AddressDto address = new AddressDto();
}
