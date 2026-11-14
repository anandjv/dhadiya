package com.dhadiya.flow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCompletionRequest {

    @NotBlank(message = "status is required (APPROVED, REJECTED, COMPLETED)")
    private String status;

    private String remarks;

    private String completedBy;
}
