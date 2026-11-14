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
public class WorkflowStartCaseDto {

    @NotBlank(message = "Realm code is required")
    private String realmCode;

    @NotBlank(message = "Workflow code is required")
    private String workflowCode;

    @NotBlank(message = "Initiator user email is required")
    private String initiatedBy;

    private String priority;
    private String initialPayloadJson;
}
