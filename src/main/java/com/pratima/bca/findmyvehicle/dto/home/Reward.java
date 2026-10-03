package com.pratima.bca.findmyvehicle.dto.home;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reward {
    private Double amount;

    private String currency;

    private String displayName;
}
