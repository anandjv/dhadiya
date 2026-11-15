package com.dhadiya.flow.dto;

import com.dhadiya.flow.entity.FormMaster;
import com.dhadiya.flow.entity.WorkflowMaster;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessBundleDto {
    private String realmCode;
    private String processKey;
    private String name;
    private String category;
    private String industryType;
    private String description;

    // Dynamic Virtual Table Definition
    private String tableName;
    private String tableLabel;
    private String tableDescription;
    private List<String> tableColumns;
    private String tableSchemaJson;

    // Dynamic Form Master
    private FormMaster form;

    // Dynamic Workflow Master
    private WorkflowMaster workflow;
}
