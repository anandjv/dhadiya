package com.dhadiya.flow.service;

import com.dhadiya.flow.dto.ProcessStartRequest;
import com.dhadiya.flow.dto.ProcessTransitionRequest;
import com.dhadiya.flow.dto.TaskCompletionRequest;
import com.dhadiya.flow.entity.ProcessDefinition;
import com.dhadiya.flow.entity.ProcessInstance;
import com.dhadiya.flow.entity.ProcessTask;
import com.dhadiya.flow.repository.ProcessDefinitionRepository;
import com.dhadiya.flow.repository.ProcessInstanceRepository;
import com.dhadiya.flow.repository.ProcessTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowOrchestrationService {

    private final ProcessDefinitionRepository definitionRepository;
    private final ProcessInstanceRepository instanceRepository;
    private final ProcessTaskRepository taskRepository;
    private final DynamicGridService gridService;

    @Transactional(readOnly = true)
    public List<ProcessDefinition> getDefinitions(String realmCode) {
        return definitionRepository.findByRealmCode(realmCode);
    }

    @Transactional
    public ProcessDefinition saveDefinition(ProcessDefinition definition) {
        return definitionRepository.save(definition);
    }

    @Transactional
    public ProcessInstance startProcess(ProcessStartRequest request) {
        String trackingNum = "DHADIYA-" + System.currentTimeMillis() % 1000000;
        String stage = (request.getInitialStage() != null && !request.getInitialStage().isBlank())
                ? request.getInitialStage()
                : "INITIAL_SUBMISSION";

        ProcessInstance instance = ProcessInstance.builder()
                .trackingNumber(trackingNum)
                .realmCode(request.getRealmCode())
                .processKey(request.getProcessKey())
                .currentStage(stage)
                .status("IN_PROGRESS")
                .initiatedBy(request.getInitiatedBy())
                .payloadJson(request.getPayloadJson())
                .build();

        ProcessInstance saved = instanceRepository.save(instance);

        ProcessTask task = ProcessTask.builder()
                .instanceId(saved.getId())
                .trackingNumber(saved.getTrackingNumber())
                .stageName(stage)
                .title("Process Case: " + trackingNum)
                .assignee(request.getInitiatedBy())
                .status("PENDING")
                .build();
        taskRepository.save(task);

        // Synchronize with dynamic data grid in the middleware
        try {
            gridService.syncFromProcessInstance(saved);
        } catch (Exception ignored) {
        }

        return saved;
    }

    @Transactional
    public ProcessInstance transition(String trackingNumber, ProcessTransitionRequest request) {
        ProcessInstance instance = instanceRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new IllegalArgumentException("Process instance not found: " + trackingNumber));

        instance.setCurrentStage(request.getTargetStage());
        if (request.getUpdatedPayloadJson() != null) {
            instance.setPayloadJson(request.getUpdatedPayloadJson());
        }

        if ("COMPLETE".equalsIgnoreCase(request.getAction()) || "APPROVED_FINAL".equalsIgnoreCase(request.getAction())) {
            instance.setStatus("COMPLETED");
            instance.setCompletedAt(LocalDateTime.now());
        } else if ("REJECT".equalsIgnoreCase(request.getAction())) {
            instance.setStatus("REJECTED");
            instance.setCompletedAt(LocalDateTime.now());
        }

        ProcessInstance saved = instanceRepository.save(instance);

        if ("IN_PROGRESS".equals(saved.getStatus())) {
            ProcessTask nextTask = ProcessTask.builder()
                    .instanceId(saved.getId())
                    .trackingNumber(saved.getTrackingNumber())
                    .stageName(request.getTargetStage())
                    .title("Action required for stage: " + request.getTargetStage())
                    .status("PENDING")
                    .remarks(request.getRemarks())
                    .build();
            taskRepository.save(nextTask);
        }

        // Synchronize updated state into dynamic data grid in the middleware
        try {
            gridService.syncFromProcessInstance(saved);
        } catch (Exception ignored) {
        }

        return saved;
    }

    @Transactional(readOnly = true)
    public List<ProcessInstance> getInstances(String realmCode) {
        return instanceRepository.findByRealmCode(realmCode);
    }

    @Transactional(readOnly = true)
    public ProcessInstance getInstance(String trackingNumber) {
        return instanceRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new IllegalArgumentException("Process instance not found: " + trackingNumber));
    }

    @Transactional(readOnly = true)
    public List<ProcessTask> getPendingTasks(String assignee) {
        if (assignee != null && !assignee.isBlank()) {
            return taskRepository.findByAssigneeAndStatus(assignee, "PENDING");
        }
        return taskRepository.findByStatus("PENDING");
    }

    @Transactional
    public ProcessTask completeTask(Long taskId, TaskCompletionRequest request) {
        ProcessTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        task.setStatus(request.getStatus());
        task.setRemarks(request.getRemarks());
        task.setCompletedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }
}
