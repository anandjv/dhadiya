package com.dhadiya.flow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormTemplateRequest {

    @NotBlank(message = "realmCode is required")
    private String realmCode;

    @NotBlank(message = "templateKey is required")
    private String templateKey;

    @NotBlank(message = "title is required")
    private String title;

    private Integer version;

    private String schemaJson;
}
