package com.pratima.bca.findmyvehicle.entity.vehicle;

import com.pratima.bca.findmyvehicle.entity.User;
import com.pratima.bca.findmyvehicle.enums.VehicleCompany;
import com.pratima.bca.findmyvehicle.enums.VehicleStatus;
import com.pratima.bca.findmyvehicle.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tbl_vehicle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="REG_NUMBER", nullable = false, unique = true)
    private String regNumber;

    @Column(name="VEHICLE_CHASSIS_NUMBER", nullable = false, unique = true)
    private String chassisNumber;

    @Column(length = 100, name="VEHICLE_ENGINE_NUMBER")
    private String engineNumber;

    @Column(name="VEHICLE_OWNER", nullable = false, unique = false)
    private String owner;

    @Column(name="OWNER_EMAIL", nullable = false)
    private String ownerEmail;

    @Column(name="OWNER_MOBILE", nullable = false)
    private String ownerMobile;

    @Column(name="VEHICLE_COLOR")
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name="VEHICLE_TYPE")
    private VehicleType type;

    @Enumerated(EnumType.STRING)
    @Column(name="VEHICLE_COMPANY", length = 50)
    private VehicleCompany vehicleCompany;

    @Enumerated(EnumType.STRING)
    @Column(name="VEHICLE_STATUS")
    private VehicleStatus vehicleStatus;

    @Column(nullable = false, length = 50, name="VEHICLE_MODEL")
    private String vehicleModel;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VehicleImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MissingDetails> missingDetails = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by", nullable = false)
    private User reportedBy;

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
