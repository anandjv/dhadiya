package com.dhadiya.tenants.dto;

import com.dhadiya.tenants.entity.TenantUser;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantUserResponse {

    private Long id;
    private String realmCode;
    private String email;
    private String fullName;
    private String role;
    private String showroomLocation;
    private String phoneNumber;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;

    public static TenantUserResponse fromEntity(TenantUser entity) {
        if (entity == null) return null;
        return TenantUserResponse.builder()
                .id(entity.getId())
                .realmCode(entity.getRealmCode())
                .email(entity.getEmail())
                .fullName(entity.getFullName())
                .role(entity.getRole())
                .showroomLocation(entity.getShowroomLocation())
                .phoneNumber(entity.getPhoneNumber())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .lastLoginAt(entity.getLastLoginAt())
                .build();
    }
}
