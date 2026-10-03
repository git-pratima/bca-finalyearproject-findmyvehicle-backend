package com.pratima.bca.findmyvehicle.dto.home;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsDto {

    @Builder.Default
    private String key = "";

    @Builder.Default
    private Long value = 0L;

    @Builder.Default
    private String label = "";

    @Builder.Default
    private String description = "";

    @Builder.Default
    private String icon = "";

}

