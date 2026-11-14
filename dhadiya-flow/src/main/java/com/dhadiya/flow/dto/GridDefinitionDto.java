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
public class GridDefinitionDto {

    @NotBlank(message = "Realm code is required")
    private String realmCode;

    @NotBlank(message = "Grid key is required")
    private String gridKey;

    @NotBlank(message = "Grid name is required")
    private String gridName;

    private String description;
    private String processKey;
    private Integer defaultPageSize;
    private String columnSchemaJson;
    private Boolean enableExport;
}
