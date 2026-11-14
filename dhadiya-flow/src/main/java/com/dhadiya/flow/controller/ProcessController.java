package com.dhadiya.flow.controller;

import com.dhadiya.flow.dto.ProcessStartRequest;
import com.dhadiya.flow.dto.ProcessTransitionRequest;
import com.dhadiya.flow.entity.ProcessDefinition;
import com.dhadiya.flow.entity.ProcessInstance;
import com.dhadiya.flow.service.WorkflowOrchestrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/processes")
@RequiredArgsConstructor
@Tag(name = "Dhadiya Flow API", description = "Workflow process lifecycle and instances for dhadiya.com")
public class ProcessController {

    private final WorkflowOrchestrationService orchestrationService;

    @GetMapping("/definitions")
    @Operation(summary = "Get process definitions for a realm")
    public ResponseEntity<List<ProcessDefinition>> getDefinitions(
            @RequestParam(defaultValue = "dhadiya-showroom-global") String realmCode) {
        return ResponseEntity.ok(orchestrationService.getDefinitions(realmCode));
    }

    @PostMapping("/definitions")
    @Operation(summary = "Publish a process definition")
    public ResponseEntity<ProcessDefinition> saveDefinition(@RequestBody ProcessDefinition definition) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orchestrationService.saveDefinition(definition));
    }

    @PostMapping("/instances/start")
    @Operation(summary = "Initiate a new process instance")
    public ResponseEntity<ProcessInstance> startProcess(@Valid @RequestBody ProcessStartRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orchestrationService.startProcess(request));
    }

    @PostMapping("/instances/{trackingNumber}/transition")
    @Operation(summary = "Advance process instance stage")
    public ResponseEntity<ProcessInstance> transition(
            @PathVariable String trackingNumber,
            @Valid @RequestBody ProcessTransitionRequest request) {
        return ResponseEntity.ok(orchestrationService.transition(trackingNumber, request));
    }

    @GetMapping("/instances")
    @Operation(summary = "List process instances by realm")
    public ResponseEntity<List<ProcessInstance>> getInstances(
            @RequestParam(defaultValue = "dhadiya-showroom-global") String realmCode) {
        return ResponseEntity.ok(orchestrationService.getInstances(realmCode));
    }

    @GetMapping("/instances/{trackingNumber}")
    @Operation(summary = "Get process instance status")
    public ResponseEntity<ProcessInstance> getInstance(@PathVariable String trackingNumber) {
        return ResponseEntity.ok(orchestrationService.getInstance(trackingNumber));
    }
}
