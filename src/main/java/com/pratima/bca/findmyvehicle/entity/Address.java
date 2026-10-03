package com.pratima.bca.findmyvehicle.entity;

import com.pratima.bca.findmyvehicle.enums.State;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="address_line1", columnDefinition = "TEXT")
    private String addressLine1;

    @Column(name="address_line2",columnDefinition = "TEXT")
    private String addressLine2;

    @Column(name="city",length = 100)
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(name="state",length = 100)
    private State state;

    @Column(name="pin_code",length = 10)
    private String pinCode;

    @Column(name="country",length = 100)
    private String country;

    @OneToOne(mappedBy = "address")
    private User user;

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

        System.out.println("User Name = " + userName);
        if(this.createdDate == null) {
            this.createdDate = new Date();
        }
        if(this.createdBy == null){
            this.createdBy = userName;
        }
    }
}
