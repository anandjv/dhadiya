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
public class GridViewDto {

    @NotBlank(message = "Realm code is required")
    private String realmCode;

    @NotBlank(message = "Grid key is required")
    private String gridKey;

    @NotBlank(message = "View name is required")
    private String viewName;

    private String filterSpecJson;
    private String sortSpecJson;
    private Boolean isDefault;
    private String createdBy;
}
