package com.dhadiya.tenants.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dhadiya_tenants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantRealm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String realmCode;

    @Column(nullable = false, length = 128)
    private String organizationName;

    @Column(length = 255)
    private String customDomain;

    @Column(length = 32)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, SUSPENDED, PROVISIONING

    @Column(columnDefinition = "TEXT")
    private String settingsJson;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
