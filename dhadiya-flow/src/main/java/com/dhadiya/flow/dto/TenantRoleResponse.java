package com.dhadiya.flow.dto;

import com.dhadiya.flow.entity.TenantRole;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantRoleResponse {

    private Long id;
    private String realmCode;
    private String roleCode;
    private String roleName;
    private String description;
    private String locationScope;
    private String permissionsJson;
    private String colorCode;
    private boolean isSystemRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TenantRoleResponse fromEntity(TenantRole entity) {
        if (entity == null) return null;
        return TenantRoleResponse.builder()
                .id(entity.getId())
                .realmCode(entity.getRealmCode())
                .roleCode(entity.getRoleCode())
                .roleName(entity.getRoleName())
                .description(entity.getDescription())
                .locationScope(entity.getLocationScope())
                .permissionsJson(entity.getPermissionsJson())
                .colorCode(entity.getColorCode())
                .isSystemRole(entity.isSystemRole())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
