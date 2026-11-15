package com.dhadiya.flow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantUserRequest {

    @NotBlank(message = "Realm code is required")
    private String realmCode;

    @NotBlank(message = "Email is required")
    @Email(message = "Valid email is required")
    private String email;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Role is required")
    private String role;

    private String locationCode;
    private String showroomLocation;
    private String phoneNumber;
    private String status;
}
