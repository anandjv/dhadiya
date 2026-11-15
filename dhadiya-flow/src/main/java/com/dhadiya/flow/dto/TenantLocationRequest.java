package com.dhadiya.flow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantLocationRequest {

    @NotBlank(message = "Realm code is required")
    private String realmCode;

    @NotBlank(message = "Location code is required")
    private String locationCode;

    @NotBlank(message = "Location name is required")
    private String locationName;

    private String city;
    private String stateProvince;
    private String country;
    private String address;
    private String phoneNumber;
    private String managerEmail;
    private String status;
}
