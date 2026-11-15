package com.dhadiya.flow.model;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowTableMaster implements Serializable {
    private Long id;
    private String tableName;
    private String tableDesc;
    @Builder.Default
    private boolean autoIncr = true;
    @Builder.Default
    private List<WorkflowTableField> virtualTableFieldsList = new ArrayList<>();
}
