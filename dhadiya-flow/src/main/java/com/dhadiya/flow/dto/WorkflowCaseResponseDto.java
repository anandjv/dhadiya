package com.dhadiya.flow.dto;

import com.dhadiya.flow.entity.WorkflowStage;
import com.dhadiya.flow.entity.WorkflowTrackDet;
import com.dhadiya.flow.entity.WorkflowTrackMaster;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowCaseResponseDto {

    private WorkflowTrackMaster trackMaster;
    private List<WorkflowStage> workflowStages;
    private List<String> availableActions;
    private List<WorkflowTrackDet> historyTrail;
}
