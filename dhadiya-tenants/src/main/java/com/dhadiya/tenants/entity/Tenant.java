package com.dhadiya.tenants.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenants", indexes = {
        @Index(name = "idx_tenants_tenant_id", columnList = "tenant_id", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Tenant Organization Data ---
    @Column(name = "tenant_id", unique = true, nullable = false, length = 100)
    private String tenantId;

    @Column(name = "tenant_name", nullable = false, length = 200)
    private String tenantName;

    @Column(name = "address", length = 300)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "zip_code", length = 30)
    private String zipCode;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "custom_domain", length = 255)
    private String customDomain;

    @Column(name = "status", length = 32)
    @Builder.Default
    private String status = "ACTIVE";

    @Column(name = "settings_json", columnDefinition = "TEXT")
    private String settingsJson;

    // --- Merged DB Connection Data (Single Table) ---
    @Column(name = "driver_name", length = 150)
    @Builder.Default
    private String driverName = "org.postgresql.Driver";

    @Column(name = "url", length = 500)
    private String url;

    @Column(name = "user_name", length = 100)
    private String userName;

    @Column(name = "password", length = 200)
    private String password;

    @Column(name = "mongo_uri", length = 500)
    private String mongoUri;

    // --- Timestamps ---
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Compatibility methods for Realm
    public String getRealmCode() {
        return this.tenantId;
    }

    public void setRealmCode(String realmCode) {
        this.tenantId = realmCode;
    }

    public String getOrganizationName() {
        return this.tenantName;
    }

    public void setOrganizationName(String orgName) {
        this.tenantName = orgName;
    }
}
