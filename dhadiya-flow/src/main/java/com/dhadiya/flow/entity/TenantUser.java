package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dhadiya_tenant_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String realmCode;

    @Column(nullable = false, unique = true, length = 128)
    private String email;

    @Column(nullable = false, length = 128)
    private String fullName;

    @Column(nullable = false, length = 64)
    private String role; // SHOWROOM_DIRECTOR, PDI_TECHNICIAN, DETAILING_SPECIALIST, FINANCE_MANAGER, SALES_ADVISOR, BRAND_AUDITOR

    @Column(length = 64)
    private String locationCode;

    @Column(length = 128)
    private String showroomLocation;

    @Column(length = 32)
    private String phoneNumber;

    @Column(length = 32)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, INVITED, SUSPENDED

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime lastLoginAt;
}
