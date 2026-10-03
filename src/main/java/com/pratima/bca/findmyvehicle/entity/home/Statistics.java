package com.pratima.bca.findmyvehicle.entity.home;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_statistics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="key")
    private String key;

    @Column(name="value")
    private String value;

    @Column(name="label")
    private String label;

    @Column(name="description")
    private String description;

    @Column(name="icon")
    private String icon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_dash_id", nullable = false)
    private HomeDashData homeDashData;
}
