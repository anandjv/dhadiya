package com.dhadiya.tenants.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantRealmRequest {

    @NotBlank(message = "realmCode is required")
    @Size(min = 2, max = 64)
    private String realmCode;

    @NotBlank(message = "organizationName is required")
    @Size(min = 2, max = 128)
    private String organizationName;

    private String customDomain;

    private String settingsJson;
}
