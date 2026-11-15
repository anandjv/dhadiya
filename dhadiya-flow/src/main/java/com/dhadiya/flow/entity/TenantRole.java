package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dhadiya_tenant_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String realmCode; // e.g. tvs-motors, GLOBAL

    @Column(nullable = false, length = 64)
    private String roleCode; // e.g. EV_DIAGNOSTIC_SPECIALIST

    @Column(nullable = false, length = 128)
    private String roleName; // e.g. High-Voltage EV Diagnostics Lead

    @Column(length = 255)
    private String description;

    @Column(nullable = false, length = 64)
    @Builder.Default
    private String locationScope = "ALL"; // ALL or specific locationCode (e.g. TVS-BLR-01)

    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String permissionsJson = "[\"VIEW_DASHBOARD\"]";

    @Column(length = 32)
    @Builder.Default
    private String colorCode = "#0284c7";

    @Builder.Default
    private boolean isSystemRole = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
