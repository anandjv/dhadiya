package com.dhadiya.tenants.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dhadiya_tenant_locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String realmCode; // e.g. tvs-motors, dhadiya-showroom-americas

    @Column(nullable = false, unique = true, length = 64)
    private String locationCode; // e.g. TVS-BLR-01, BH-RODEO-01

    @Column(nullable = false, length = 128)
    private String locationName; // e.g. Bangalore Indiranagar Flagship

    @Column(length = 64)
    private String city;

    @Column(length = 64)
    private String stateProvince;

    @Column(length = 64)
    private String country;

    @Column(length = 255)
    private String address;

    @Column(length = 32)
    private String phoneNumber;

    @Column(length = 128)
    private String managerEmail;

    @Column(length = 32)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, UNDER_RENOVATION, OPENING_SOON

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
