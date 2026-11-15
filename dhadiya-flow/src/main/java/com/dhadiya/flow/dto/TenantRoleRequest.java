package com.dhadiya.flow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantRoleRequest {

    @NotBlank(message = "Realm code is required")
    private String realmCode;

    @NotBlank(message = "Role code is required")
    private String roleCode;

    @NotBlank(message = "Role name is required")
    private String roleName;

    private String description;
    private String locationScope; // ALL or specific locationCode
    private List<String> permissions;
    private String colorCode;
}
