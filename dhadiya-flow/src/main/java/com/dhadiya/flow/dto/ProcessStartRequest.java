package com.dhadiya.flow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessStartRequest {

    @NotBlank(message = "realmCode is required")
    private String realmCode;

    @NotBlank(message = "processKey is required")
    private String processKey;

    private String initiatedBy;

    private String initialStage;

    private String payloadJson;
}
