package com.dhadiya.flow.controller;

import com.dhadiya.flow.dto.TaskCompletionRequest;
import com.dhadiya.flow.entity.ProcessTask;
import com.dhadiya.flow.service.WorkflowOrchestrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Dhadiya Process Tasks API", description = "Task queues and stage approval actions for dhadiya.com")
public class ProcessTaskController {

    private final WorkflowOrchestrationService orchestrationService;

    @GetMapping("/pending")
    @Operation(summary = "Get pending approval tasks")
    public ResponseEntity<List<ProcessTask>> getPendingTasks(
            @RequestParam(required = false) String assignee) {
        return ResponseEntity.ok(orchestrationService.getPendingTasks(assignee));
    }

    @PostMapping("/{taskId}/action")
    @Operation(summary = "Approve, reject, or complete task")
    public ResponseEntity<ProcessTask> completeTask(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskCompletionRequest request) {
        return ResponseEntity.ok(orchestrationService.completeTask(taskId, request));
    }
}
