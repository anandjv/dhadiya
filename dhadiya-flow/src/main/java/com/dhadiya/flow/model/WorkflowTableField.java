package com.dhadiya.flow.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowTableField implements Serializable {
    private Long id;
    private String fieldName;
    private WorkflowTableFieldDataType fieldDataType;
    @Builder.Default
    private Integer size = 255;
    private String fieldDesc;
    private boolean required;
    private String defaultValue;
}
