package com.pratima.bca.findmyvehicle.entity.home;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_home_dash")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeDashData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="EYEBROW", nullable = false, unique = true)
    private String eyebrow;

    @Column(name="TITLE", nullable = false, unique = true)
    private String title;

    @Column(name="HIGHLIGHTED_WORD", nullable = false, unique = true)
    private String highlightedWord;

    @Column(name="DESCRIPTION", nullable = false, unique = true)
    private String description;

    @Column(name="SEARCH_PLACE_HOLDER", nullable = false, unique = true)
    private String searchPlaceholder;

    @Column(name="REPORT_MISSING_URL", nullable = false, unique = true)
    private String reportMissingUrl;

    @Column(name="SEARCH_VEHICLE_URL", nullable = false, unique = true)
    private String searchVehiclesUrl;

    @OneToMany(mappedBy = "homeDashData", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Statistics> statistics = new ArrayList<>();

}
