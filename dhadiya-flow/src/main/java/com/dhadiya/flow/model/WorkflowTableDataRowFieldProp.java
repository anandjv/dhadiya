package com.dhadiya.flow.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkflowTableDataRowFieldProp implements Serializable {
    private Object fieldValue;
    private WorkflowTableFieldDataType fieldDataType;
    private String tableName;
    private String fieldName;
}
