package com.dhadiya.flow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessTransitionRequest {

    @NotBlank(message = "targetStage is required")
    private String targetStage;

    @NotBlank(message = "action is required (e.g. ADVANCE, COMPLETE, REJECT)")
    private String action;

    private String updatedPayloadJson;

    private String executedBy;

    private String remarks;
}
