package com.dhadiya.tenants.dto;

import com.dhadiya.tenants.entity.TenantRealm;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantRealmResponse {
    private Long id;
    private String realmCode;
    private String organizationName;
    private String customDomain;
    private String status;
    private String settingsJson;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TenantRealmResponse fromEntity(TenantRealm entity) {
        return TenantRealmResponse.builder()
                .id(entity.getId())
                .realmCode(entity.getRealmCode())
                .organizationName(entity.getOrganizationName())
                .customDomain(entity.getCustomDomain())
                .status(entity.getStatus())
                .settingsJson(entity.getSettingsJson())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
