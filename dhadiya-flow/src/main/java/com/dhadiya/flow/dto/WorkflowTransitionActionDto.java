package com.dhadiya.flow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTransitionActionDto {

    @NotBlank(message = "Action name is required (e.g. APPROVE, REJECT, SUBMIT, REWORK)")
    private String actionName;

    @NotBlank(message = "Actor email is required")
    private String actedBy;

    private String targetStageCode; // optional override
    private String remarks;
    private String updatedPayloadJson;
}
