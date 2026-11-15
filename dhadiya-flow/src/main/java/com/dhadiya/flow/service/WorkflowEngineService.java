package com.dhadiya.flow.service;

import com.dhadiya.flow.dto.GridRecordDto;
import com.dhadiya.flow.dto.WorkflowCaseResponseDto;
import com.dhadiya.flow.dto.WorkflowStartCaseDto;
import com.dhadiya.flow.dto.WorkflowTransitionActionDto;
import com.dhadiya.flow.entity.*;
import com.dhadiya.flow.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkflowEngineService {

    private final WorkflowMasterRepository workflowRepository;
    private final WorkflowTrackMasterRepository trackMasterRepository;
    private final WorkflowTrackDetRepository trackDetRepository;
    private final WorkflowTaskItemRepository taskRepository;
    private final DynamicGridService gridService;

    @Transactional(readOnly = true)
    public List<WorkflowMaster> listWorkflows(String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            return workflowRepository.findByRealmCode(realmCode);
        }
        return workflowRepository.findAll();
    }

    @Transactional(readOnly = true)
    public WorkflowMaster getWorkflow(String workflowCode) {
        return workflowRepository.findByWorkflowCode(workflowCode)
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found: " + workflowCode));
    }

    @Transactional(readOnly = true)
    public WorkflowMaster saveWorkflow(WorkflowMaster workflow) {
        return workflowRepository.save(workflow);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getKanbanBoard(String workflowCode, String realmCode) {
        WorkflowMaster wf = (workflowCode != null && !workflowCode.isBlank())
                ? workflowRepository.findByWorkflowCode(workflowCode).orElseGet(() -> workflowRepository.findAll().stream().findFirst().orElse(null))
                : workflowRepository.findAll().stream().findFirst().orElse(null);
        if (wf == null) return Collections.emptyMap();

        List<WorkflowTrackMaster> cases = (realmCode != null && !realmCode.isBlank())
                ? trackMasterRepository.findByRealmCodeAndWorkflowCode(realmCode, wf.getWorkflowCode())
                : trackMasterRepository.findAll().stream()
                    .filter(c -> wf.getWorkflowCode().equals(c.getWorkflowCode()))
                    .collect(Collectors.toList());

        List<Map<String, Object>> columns = new ArrayList<>();
        List<WorkflowStage> stages = wf.getStages().stream()
                .sorted(Comparator.comparingInt(WorkflowStage::getStageOrder))
                .collect(Collectors.toList());

        for (WorkflowStage stage : stages) {
            Map<String, Object> col = new LinkedHashMap<>();
            col.put("stageCode", stage.getStageCode());
            col.put("stageName", stage.getStageName());
            col.put("stageOrder", stage.getStageOrder());
            col.put("colorCode", stage.getStageColor());
            List<WorkflowTrackMaster> colCases = cases.stream()
                    .filter(c -> stage.getStageCode().equalsIgnoreCase(c.getCurrentStageCode()))
                    .collect(Collectors.toList());
            col.put("cases", colCases);
            col.put("count", colCases.size());
            columns.add(col);
        }

        Map<String, Object> board = new LinkedHashMap<>();
        board.put("workflowCode", wf.getWorkflowCode());
        board.put("workflowName", wf.getWorkflowName());
        board.put("columns", columns);
        return board;
    }

    @Transactional
    public WorkflowTrackMaster initiateCase(WorkflowStartCaseDto dto) {
        WorkflowMaster wf = workflowRepository.findByWorkflowCode(dto.getWorkflowCode())
                .orElseThrow(() -> new IllegalArgumentException("Invalid workflow code: " + dto.getWorkflowCode()));

        List<WorkflowStage> stages = wf.getStages().stream()
                .sorted(Comparator.comparingInt(WorkflowStage::getStageOrder))
                .collect(Collectors.toList());

        if (stages.isEmpty()) {
            throw new IllegalStateException("Workflow has no defined stages: " + dto.getWorkflowCode());
        }

        WorkflowStage startStage = stages.get(0);
        String year = String.valueOf(LocalDateTime.now().getYear());
        long randomSuffix = (System.currentTimeMillis() % 900000L) + 100000L;
        String caseNumber = "DHADIYA-" + year + "-" + randomSuffix;

        WorkflowTrackMaster track = WorkflowTrackMaster.builder()
                .caseNumber(caseNumber)
                .realmCode(dto.getRealmCode())
                .workflowCode(wf.getWorkflowCode())
                .workflowName(wf.getWorkflowName())
                .currentStageCode(startStage.getStageCode())
                .currentStageName(startStage.getStageName())
                .status("IN_PROGRESS")
                .priority(dto.getPriority() != null ? dto.getPriority() : "MEDIUM")
                .initiatedBy(dto.getInitiatedBy())
                .initiatedOn(LocalDateTime.now())
                .lastUpdatedOn(LocalDateTime.now())
                .tableName(wf.getTableName())
                .dataPayloadJson(dto.getInitialPayloadJson() != null ? dto.getInitialPayloadJson() : "{}")
                .build();

        WorkflowTrackMaster savedCase = trackMasterRepository.save(track);

        // Record Initial Audit Log (WorkflowTrackDet)
        WorkflowTrackDet initialAudit = WorkflowTrackDet.builder()
                .caseNumber(caseNumber)
                .stageCode(startStage.getStageCode())
                .stageName(startStage.getStageName())
                .actionTaken("INITIATE")
                .actedBy(dto.getInitiatedBy())
                .remarks("Case initiated under workflow " + wf.getWorkflowName())
                .performedOn(LocalDateTime.now())
                .durationSeconds(0L)
                .snapshotDataJson(savedCase.getDataPayloadJson())
                .build();
        trackDetRepository.save(initialAudit);

        // Generate First Task Item
        String assignee = startStage.getDefaultAssignee() != null && !startStage.getDefaultAssignee().isBlank()
                ? startStage.getDefaultAssignee()
                : dto.getInitiatedBy();

        int slaHours = startStage.getSlaHours() != null ? startStage.getSlaHours() : 48;
        WorkflowTaskItem task = WorkflowTaskItem.builder()
                .caseNumber(caseNumber)
                .stageCode(startStage.getStageCode())
                .taskTitle("Action for " + startStage.getStageName() + " (" + caseNumber + ")")
                .assignedTo(assignee)
                .roleName(startStage.getAllowedRole())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .slaDueDate(LocalDateTime.now().plusHours(slaHours))
                .build();
        taskRepository.save(task);

        // Synchronize with dynamic data grid
        if (wf.getTableName() != null && !wf.getTableName().isBlank()) {
            try {
                gridService.insertRecord(GridRecordDto.builder()
                        .realmCode(dto.getRealmCode())
                        .gridKey(wf.getTableName())
                        .processTrackingNumber(caseNumber)
                        .dataJson(savedCase.getDataPayloadJson())
                        .build());
            } catch (Exception e) {
                log.warn("Auto-sync to data grid skipped: {}", e.getMessage());
            }
        }

        log.info("Initiated Workflow Case: {} for workflow {}", caseNumber, wf.getWorkflowCode());
        return savedCase;
    }

    @Transactional
    public WorkflowTrackMaster executeAction(String caseNumber, WorkflowTransitionActionDto dto) {
        WorkflowTrackMaster track = trackMasterRepository.findByCaseNumber(caseNumber)
                .orElseThrow(() -> new IllegalArgumentException("Case not found: " + caseNumber));

        if (!"IN_PROGRESS".equalsIgnoreCase(track.getStatus())) {
            throw new IllegalStateException("Case is already in terminal state: " + track.getStatus());
        }

        WorkflowMaster wf = workflowRepository.findByWorkflowCode(track.getWorkflowCode())
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found: " + track.getWorkflowCode()));

        String currentStage = track.getCurrentStageCode();
        String action = dto.getActionName().trim().toUpperCase();

        // 1. Identify Target Stage via Triggers
        Optional<WorkflowTrigger> triggerOpt = wf.getTriggers().stream()
                .filter(t -> t.getSourceStageCode().equalsIgnoreCase(currentStage) && t.getActionName().equalsIgnoreCase(action))
                .findFirst();

        String nextStageCode;
        if (dto.getTargetStageCode() != null && !dto.getTargetStageCode().isBlank()) {
            nextStageCode = dto.getTargetStageCode().trim().toUpperCase();
        } else if (triggerOpt.isPresent()) {
            nextStageCode = triggerOpt.get().getTargetStageCode();
        } else {
            // Find stage with next order if simple sequence
            List<WorkflowStage> sorted = wf.getStages().stream()
                    .sorted(Comparator.comparingInt(WorkflowStage::getStageOrder))
                    .collect(Collectors.toList());

            int curIdx = -1;
            for (int i = 0; i < sorted.size(); i++) {
                if (sorted.get(i).getStageCode().equalsIgnoreCase(currentStage)) {
                    curIdx = i;
                    break;
                }
            }

            if ("REJECT".equals(action)) {
                nextStageCode = "REJECTED";
            } else if (curIdx != -1 && curIdx + 1 < sorted.size()) {
                nextStageCode = sorted.get(curIdx + 1).getStageCode();
            } else {
                nextStageCode = "COMPLETED";
            }
        }

        // 2. Calculate duration
        LocalDateTime now = LocalDateTime.now();
        long durationSec = Duration.between(track.getLastUpdatedOn(), now).getSeconds();

        // 3. Close open task item for current stage
        Optional<WorkflowTaskItem> currentTaskOpt = taskRepository.findByCaseNumberAndStageCodeAndStatus(
                caseNumber, currentStage, "PENDING");
        if (currentTaskOpt.isPresent()) {
            WorkflowTaskItem curTask = currentTaskOpt.get();
            curTask.setStatus("COMPLETED");
            curTask.setCompletedAt(now);
            curTask.setActionTaken(action);
            curTask.setRemarks(dto.getRemarks());
            taskRepository.save(curTask);
        }

        // 4. Update Case State
        track.setLastUpdatedOn(now);
        if (dto.getUpdatedPayloadJson() != null && !dto.getUpdatedPayloadJson().isBlank()) {
            track.setDataPayloadJson(dto.getUpdatedPayloadJson());
        }

        WorkflowStage nextStage = wf.getStages().stream()
                .filter(s -> s.getStageCode().equalsIgnoreCase(nextStageCode))
                .findFirst().orElse(null);

        boolean isTerminal = "COMPLETED".equalsIgnoreCase(nextStageCode)
                || "REJECTED".equalsIgnoreCase(nextStageCode)
                || (nextStage != null && "END".equalsIgnoreCase(nextStage.getStageType()));

        if (isTerminal) {
            track.setStatus("REJECTED".equalsIgnoreCase(nextStageCode) ? "REJECTED" : "COMPLETED");
            track.setCurrentStageCode(nextStageCode);
            track.setCurrentStageName(nextStage != null ? nextStage.getStageName() : nextStageCode);
            track.setCompletedOn(now);
        } else {
            track.setCurrentStageCode(nextStageCode);
            track.setCurrentStageName(nextStage != null ? nextStage.getStageName() : nextStageCode);

            // Create Next Task Item
            String nextAssignee = (nextStage != null && nextStage.getDefaultAssignee() != null)
                    ? nextStage.getDefaultAssignee()
                    : dto.getActedBy();

            int nextSla = (nextStage != null && nextStage.getSlaHours() != null) ? nextStage.getSlaHours() : 48;
            WorkflowTaskItem nextTask = WorkflowTaskItem.builder()
                    .caseNumber(caseNumber)
                    .stageCode(nextStageCode)
                    .taskTitle("Stage: " + (nextStage != null ? nextStage.getStageName() : nextStageCode) + " (" + caseNumber + ")")
                    .assignedTo(nextAssignee)
                    .roleName(nextStage != null ? nextStage.getAllowedRole() : null)
                    .status("PENDING")
                    .createdAt(now)
                    .slaDueDate(now.plusHours(nextSla))
                    .build();
            taskRepository.save(nextTask);
        }

        WorkflowTrackMaster updatedCase = trackMasterRepository.save(track);

        // 5. Append Lifecycle History (WorkflowTrackDet)
        WorkflowTrackDet audit = WorkflowTrackDet.builder()
                .caseNumber(caseNumber)
                .stageCode(nextStageCode)
                .stageName(nextStage != null ? nextStage.getStageName() : nextStageCode)
                .actionTaken(action)
                .actedBy(dto.getActedBy())
                .remarks(dto.getRemarks())
                .performedOn(now)
                .durationSeconds(durationSec)
                .previousStageCode(currentStage)
                .snapshotDataJson(updatedCase.getDataPayloadJson())
                .build();
        trackDetRepository.save(audit);

        // 6. Sync updated state into DataGrid dynamic table
        if (wf.getTableName() != null && !wf.getTableName().isBlank()) {
            try {
                gridService.insertRecord(GridRecordDto.builder()
                        .realmCode(track.getRealmCode())
                        .gridKey(wf.getTableName())
                        .processTrackingNumber(caseNumber)
                        .dataJson(updatedCase.getDataPayloadJson())
                        .build());
            } catch (Exception e) {
                log.warn("Auto-sync to data grid skipped on transition: {}", e.getMessage());
            }
        }

        log.info("Transitioned Case {}: [{}] -> [{}] via action {}", caseNumber, currentStage, nextStageCode, action);
        return updatedCase;
    }

    @Transactional(readOnly = true)
    public WorkflowCaseResponseDto getCaseDetails(String caseNumber) {
        WorkflowTrackMaster track = trackMasterRepository.findByCaseNumber(caseNumber)
                .orElseThrow(() -> new IllegalArgumentException("Case not found: " + caseNumber));

        WorkflowMaster wf = workflowRepository.findByWorkflowCode(track.getWorkflowCode()).orElse(null);
        List<WorkflowStage> stages = wf != null ? wf.getStages() : Collections.emptyList();

        List<String> actions = new ArrayList<>();
        if ("IN_PROGRESS".equalsIgnoreCase(track.getStatus()) && wf != null) {
            String curStage = track.getCurrentStageCode();
            List<String> triggerActions = wf.getTriggers().stream()
                    .filter(t -> t.getSourceStageCode().equalsIgnoreCase(curStage))
                    .map(WorkflowTrigger::getActionName)
                    .collect(Collectors.toList());

            if (!triggerActions.isEmpty()) {
                actions = triggerActions;
            } else {
                WorkflowStage s = stages.stream()
                        .filter(st -> st.getStageCode().equalsIgnoreCase(curStage))
                        .findFirst().orElse(null);
                if (s != null && s.getActionsAllowed() != null) {
                    actions = Arrays.stream(s.getActionsAllowed().split(","))
                            .map(String::trim)
                            .collect(Collectors.toList());
                } else {
                    actions = List.of("APPROVE", "REJECT");
                }
            }
        }

        List<WorkflowTrackDet> history = trackDetRepository.findByCaseNumberOrderByPerformedOnAsc(caseNumber);

        return WorkflowCaseResponseDto.builder()
                .trackMaster(track)
                .workflowStages(stages)
                .availableActions(actions)
                .historyTrail(history)
                .build();
    }

    @Transactional(readOnly = true)
    public List<WorkflowTrackDet> getCaseHistory(String caseNumber) {
        return trackDetRepository.findByCaseNumberOrderByPerformedOnAsc(caseNumber);
    }

    @Transactional(readOnly = true)
    public List<WorkflowTrackMaster> listCases(String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            return trackMasterRepository.findByRealmCode(realmCode);
        }
        return trackMasterRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<WorkflowTaskItem> getPendingTasks(String assignedTo, String roleName) {
        if (assignedTo != null && !assignedTo.isBlank()) {
            return taskRepository.findByAssignedToAndStatus(assignedTo, "PENDING");
        }
        if (roleName != null && !roleName.isBlank()) {
            return taskRepository.findByRoleNameAndStatus(roleName, "PENDING");
        }
        return taskRepository.findByStatus("PENDING");
    }

    @Transactional
    public WorkflowTaskItem completeTaskDirectly(Long taskId, String action, String remarks) {
        WorkflowTaskItem task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        task.setStatus("COMPLETED");
        task.setActionTaken(action != null ? action : "COMPLETED");
        task.setRemarks(remarks);
        task.setCompletedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }
}
