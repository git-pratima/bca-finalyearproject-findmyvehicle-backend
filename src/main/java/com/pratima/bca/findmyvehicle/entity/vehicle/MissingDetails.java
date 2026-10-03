package com.pratima.bca.findmyvehicle.entity.vehicle;

import com.pratima.bca.findmyvehicle.enums.Country;
import com.pratima.bca.findmyvehicle.enums.State;
import com.pratima.bca.findmyvehicle.enums.VehicleStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "tbl_missing_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Missing Information
     */

    @Column(name="MISSING_DATE",nullable = false)
    private LocalDate missingDate;

    @Column(name="MISSING_TIME")
    private LocalTime missingTime;

    @Column(name="FOUND_DATE",nullable = true)
    private LocalDate foundDate;

    @Enumerated(EnumType.STRING)
    @Column(name="COUNTRY",length = 100)
    private Country country;

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private State state;

    @Column(length = 100)
    private String district;

    @Column(length = 100)
    private String city;

    @Column(length = 10)
    private String pinCode;

    @Column(columnDefinition = "TEXT")
    private String missingAddress;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Enumerated(EnumType.STRING)
    @Column(name="VEHICLE_STATUS")
    private VehicleStatus vehicleStatus;

    private String reward;

    @Column(name="CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name="CREATED_BY")
    private String createdBy;

    @Column(name="MODIFIED_DATE")
    private Date modifiedDate;

    @Column(name="MODIFIED_BY")
    private String modifiedBy;

    @PreUpdate
    @PrePersist
    public void updateTimeStamps()
    {
        String userName = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            userName = authentication.getName();
        }
        this.modifiedDate = new Date();
        this.modifiedBy = userName;
        if(this.createdDate == null) {
            this.createdDate = new Date();
            this.createdBy = userName;
        }
    }

}
