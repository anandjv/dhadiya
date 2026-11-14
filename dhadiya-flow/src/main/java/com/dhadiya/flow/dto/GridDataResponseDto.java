package com.dhadiya.flow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GridDataResponseDto {

    private String gridKey;
    private String gridName;
    private String realmCode;
    private List<String> columns;
    private List<Map<String, Object>> rows;
    private long totalElements;
    private int totalPages;
    private int page;
    private int size;

    @JsonProperty("content")
    public List<Map<String, Object>> getContent() {
        return this.rows;
    }
}
