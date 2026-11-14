package com.dhadiya.tenants.dto;

import com.dhadiya.tenants.entity.TenantLocation;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantLocationResponse {

    private Long id;
    private String realmCode;
    private String locationCode;
    private String locationName;
    private String city;
    private String stateProvince;
    private String country;
    private String address;
    private String phoneNumber;
    private String managerEmail;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TenantLocationResponse fromEntity(TenantLocation entity) {
        if (entity == null) return null;
        return TenantLocationResponse.builder()
                .id(entity.getId())
                .realmCode(entity.getRealmCode())
                .locationCode(entity.getLocationCode())
                .locationName(entity.getLocationName())
                .city(entity.getCity())
                .stateProvince(entity.getStateProvince())
                .country(entity.getCountry())
                .address(entity.getAddress())
                .phoneNumber(entity.getPhoneNumber())
                .managerEmail(entity.getManagerEmail())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
