package com.dhadiya.flow.controller;

import com.dhadiya.flow.dto.WorkflowCaseResponseDto;
import com.dhadiya.flow.dto.WorkflowStartCaseDto;
import com.dhadiya.flow.dto.WorkflowTransitionActionDto;
import com.dhadiya.flow.entity.WorkflowMaster;
import com.dhadiya.flow.entity.WorkflowTaskItem;
import com.dhadiya.flow.entity.WorkflowTrackDet;
import com.dhadiya.flow.entity.WorkflowTrackMaster;
import com.dhadiya.flow.service.WorkflowEngineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Dhadiya eFormsPi Workflow Engine Core", description = "Core stateful workflow orchestration engine with lifecycle stages, triggers, task queues, and audit trail")
public class WorkflowEngineController {

    private final WorkflowEngineService engineService;

    // --- Workflow Definitions ---

    @GetMapping({"/api/v1/engine/workflows", "/workflow/workflows"})
    @Operation(summary = "List all workflows for a realm")
    public ResponseEntity<List<WorkflowMaster>> listWorkflows(
            @RequestParam(required = false, defaultValue = "dhadiya-showroom-global") String realmCode) {
        return ResponseEntity.ok(engineService.listWorkflows(realmCode));
    }

    @GetMapping("/api/v1/engine/workflows/{workflowCode}")
    @Operation(summary = "Get full workflow definition with stages and triggers")
    public ResponseEntity<WorkflowMaster> getWorkflow(@PathVariable String workflowCode) {
        return ResponseEntity.ok(engineService.getWorkflow(workflowCode));
    }

    @PostMapping({"/api/v1/engine/workflows", "/workflow/workflows"})
    @Operation(summary = "Publish or update a workflow definition")
    public ResponseEntity<WorkflowMaster> saveWorkflow(@RequestBody WorkflowMaster workflow) {
        return ResponseEntity.status(HttpStatus.CREATED).body(engineService.saveWorkflow(workflow));
    }

    // --- Case Execution & State Transitions ---

    @PostMapping({"/api/v1/engine/cases/start", "/workflowTrackMaster/workflowTrackMasters"})
    @Operation(summary = "Initiate a new workflow case instance")
    public ResponseEntity<WorkflowTrackMaster> startCase(@Valid @RequestBody WorkflowStartCaseDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(engineService.initiateCase(dto));
    }

    @GetMapping("/api/v1/engine/cases")
    @Operation(summary = "List running and completed workflow cases")
    public ResponseEntity<List<WorkflowTrackMaster>> listCases(
            @RequestParam(required = false, defaultValue = "dhadiya-showroom-global") String realmCode) {
        return ResponseEntity.ok(engineService.listCases(realmCode));
    }

    @GetMapping("/api/v1/engine/cases/{caseNumber}")
    @Operation(summary = "Get workflow case state, available actions, and stages")
    public ResponseEntity<WorkflowCaseResponseDto> getCaseDetails(@PathVariable String caseNumber) {
        return ResponseEntity.ok(engineService.getCaseDetails(caseNumber));
    }

    @GetMapping("/api/v1/engine/cases/{caseNumber}/history")
    @Operation(summary = "Get chronological lifecycle audit trail (WorkflowTrackDet)")
    public ResponseEntity<List<WorkflowTrackDet>> getCaseHistory(@PathVariable String caseNumber) {
        return ResponseEntity.ok(engineService.getCaseHistory(caseNumber));
    }

    @PostMapping("/api/v1/engine/cases/{caseNumber}/action")
    @Operation(summary = "Execute a transition action on a workflow case (APPROVE, REJECT, REWORK, ESCALATE)")
    public ResponseEntity<WorkflowTrackMaster> executeAction(
            @PathVariable String caseNumber,
            @Valid @RequestBody WorkflowTransitionActionDto dto) {
        return ResponseEntity.ok(engineService.executeAction(caseNumber, dto));
    }

    // --- Task Queues ---

    @GetMapping({"/api/v1/engine/tasks/pending", "/api/v1/tasks/pending"})
    @Operation(summary = "Get pending approval tasks for user or role")
    public ResponseEntity<List<WorkflowTaskItem>> getPendingTasks(
            @RequestParam(required = false) String assignedTo,
            @RequestParam(required = false) String roleName) {
        return ResponseEntity.ok(engineService.getPendingTasks(assignedTo, roleName));
    }

    @GetMapping({"/api/v1/engine/kanban", "/api/v1/tasks/kanban"})
    @Operation(summary = "Get Kanban board columns with case cards")
    public ResponseEntity<java.util.Map<String, Object>> getKanbanBoard(
            @RequestParam(required = false, defaultValue = "WF_SHOWROOM_DELIVERY_HANDOVER") String workflowCode,
            @RequestParam(required = false) String realmCode) {
        return ResponseEntity.ok(engineService.getKanbanBoard(workflowCode, realmCode));
    }

    @PostMapping("/api/v1/engine/tasks/{taskId}/complete")
    @Operation(summary = "Complete an individual task item directly")
    public ResponseEntity<WorkflowTaskItem> completeTask(
            @PathVariable Long taskId,
            @RequestParam(required = false, defaultValue = "COMPLETED") String action,
            @RequestParam(required = false) String remarks) {
        return ResponseEntity.ok(engineService.completeTaskDirectly(taskId, action, remarks));
    }
}
