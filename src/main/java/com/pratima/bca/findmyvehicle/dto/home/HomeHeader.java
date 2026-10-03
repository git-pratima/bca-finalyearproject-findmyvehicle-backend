package com.pratima.bca.findmyvehicle.dto.home;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeHeader {
    private String eyebrow;

    private String title;

    private String highlightedWord;

    private String description;

    private String searchPlaceholder;

    private String reportMissingUrl;

    private String searchVehiclesUrl;

    private CommunityProof communityProof;
}
