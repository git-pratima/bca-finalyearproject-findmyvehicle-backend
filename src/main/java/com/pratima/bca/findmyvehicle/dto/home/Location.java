package com.pratima.bca.findmyvehicle.dto.home;

import com.pratima.bca.findmyvehicle.enums.State;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    private String city;

    private State state;

    private String displayName;
}
