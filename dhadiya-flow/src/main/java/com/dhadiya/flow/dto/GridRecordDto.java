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
public class GridRecordDto {

    @NotBlank(message = "Realm code is required")
    private String realmCode;

    @NotBlank(message = "Grid key is required")
    private String gridKey;

    private String processTrackingNumber;

    @NotBlank(message = "Data JSON payload is required")
    private String dataJson;
}
