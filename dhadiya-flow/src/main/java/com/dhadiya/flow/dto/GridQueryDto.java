package com.dhadiya.flow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GridQueryDto {

    private String realmCode;
    private String gridKey;
    private String search;
    private Map<String, Object> filter;
    private String sortBy;
    private String sortDirection;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 20;
}
