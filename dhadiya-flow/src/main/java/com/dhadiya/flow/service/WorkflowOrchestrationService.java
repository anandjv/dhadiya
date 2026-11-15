package com.dhadiya.flow.service;

import com.dhadiya.flow.config.TenantContext;
import com.dhadiya.flow.dao.WorkflowRowRecordsDao;
import com.dhadiya.flow.dao.WorkflowTableMasterDao;
import com.dhadiya.flow.dto.ProcessBundleDto;
import com.dhadiya.flow.dto.ProcessStartRequest;
import com.dhadiya.flow.dto.ProcessTransitionRequest;
import com.dhadiya.flow.dto.TaskCompletionRequest;
import com.dhadiya.flow.entity.*;
import com.dhadiya.flow.model.WorkflowTableField;
import com.dhadiya.flow.model.WorkflowTableFieldDataType;
import com.dhadiya.flow.model.WorkflowTableMaster;
import com.dhadiya.flow.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowOrchestrationService {

    private final ProcessDefinitionRepository definitionRepository;
    private final ProcessInstanceRepository instanceRepository;
    private final ProcessTaskRepository taskRepository;
    private final DynamicGridService gridService;
    private final DynamicGridDefRepository gridDefRepository;
    private final FormMasterRepository formMasterRepository;
    private final WorkflowMasterRepository workflowMasterRepository;
    private final WorkflowTableMasterDao workflowTableMasterDao;
    private final WorkflowRowRecordsDao workflowRowRecordsDao;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional(readOnly = true)
    public List<ProcessDefinition> getDefinitions(String realmCode) {
        return definitionRepository.findByRealmCode(realmCode);
    }

    @Transactional
    public ProcessDefinition saveDefinition(ProcessDefinition definition) {
        return definitionRepository.save(definition);
    }

    @Transactional
    public ProcessBundleDto saveProcessBundle(ProcessBundleDto dto) {
        String realm = (dto.getRealmCode() != null && !dto.getRealmCode().isBlank())
                ? dto.getRealmCode()
                : TenantContext.getTenantId();
        if (realm == null || realm.isBlank()) {
            realm = "default";
        }
        TenantContext.setTenantId(realm);

        // 1. Process Definition
        ProcessDefinition proc = definitionRepository.findByRealmCodeAndProcessKey(realm, dto.getProcessKey())
                .orElse(ProcessDefinition.builder()
                        .realmCode(realm)
                        .processKey(dto.getProcessKey())
                        .build());

        proc.setName(dto.getName());
        proc.setCategory(dto.getCategory());
        proc.setIndustryType(dto.getIndustryType());
        proc.setDescription(dto.getDescription());
        proc.setTableName(dto.getTableName());
        proc.setTableSchemaJson(dto.getTableSchemaJson());
        proc.setWorkflowCode(dto.getWorkflow() != null ? dto.getWorkflow().getWorkflowCode() : dto.getProcessKey());
        if (dto.getForm() != null) {
            proc.setDefaultFormKey(dto.getForm().getFormCode());
        }
        definitionRepository.save(proc);

        // 2. Dynamic Physical Backing Table (eFormsWorkflowDBAPI DDL)
        if (dto.getTableName() != null && !dto.getTableName().isBlank()) {
            DynamicGridDef gridDef = gridDefRepository.findByRealmCodeAndGridKey(realm, dto.getTableName())
                    .orElse(DynamicGridDef.builder()
                            .realmCode(realm)
                            .gridKey(dto.getTableName())
                            .build());
            gridDef.setGridName(dto.getTableLabel() != null ? dto.getTableLabel() : dto.getName());
            gridDef.setDescription(dto.getTableDescription());
            gridDef.setProcessKey(dto.getProcessKey());
            if (dto.getTableSchemaJson() != null) {
                gridDef.setColumnSchemaJson(dto.getTableSchemaJson());
            }
            gridDefRepository.save(gridDef);

            // Execute dynamic DDL on routed tenant database
            try {
                WorkflowTableMaster tableMaster = buildWorkflowTableMaster(
                        dto.getTableName(),
                        dto.getTableDescription(),
                        dto.getTableSchemaJson(),
                        dto.getTableColumns()
                );
                workflowTableMasterDao.createTable(tableMaster);
                log.info("[LOW-CODE PLATFORM] Successfully ensured dynamic table '{}' on routed tenant database", dto.getTableName());
            } catch (Exception e) {
                log.warn("[LOW-CODE PLATFORM] Notice on dynamic DDL table creation: {}", e.getMessage());
            }
        }

        // 3. Dynamic Form
        if (dto.getForm() != null) {
            FormMaster form = dto.getForm();
            form.setRealmCode(realm);
            if (form.getTableName() == null || form.getTableName().isBlank()) {
                form.setTableName(dto.getTableName());
            }
            formMasterRepository.save(form);
        }

        // 4. Dynamic Workflow
        if (dto.getWorkflow() != null) {
            WorkflowMaster wf = dto.getWorkflow();
            wf.setRealmCode(realm);
            if (wf.getTableName() == null || wf.getTableName().isBlank()) {
                wf.setTableName(dto.getTableName());
            }
            if (dto.getForm() != null) {
                wf.setFormCode(dto.getForm().getFormCode());
            }
            workflowMasterRepository.save(wf);
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public ProcessBundleDto getProcessBundle(String realmCode, String processKey) {
        String realm = (realmCode != null && !realmCode.isBlank()) ? realmCode : "dhadiya-showroom-global";
        ProcessDefinition proc = definitionRepository.findByRealmCodeAndProcessKey(realm, processKey)
                .orElse(null);
        if (proc == null) return null;

        DynamicGridDef grid = proc.getTableName() != null
                ? gridDefRepository.findByRealmCodeAndGridKey(realm, proc.getTableName()).orElse(null)
                : null;

        FormMaster form = proc.getDefaultFormKey() != null
                ? formMasterRepository.findByRealmCodeAndFormCode(realm, proc.getDefaultFormKey()).orElse(null)
                : null;

        WorkflowMaster wf = proc.getWorkflowCode() != null
                ? workflowMasterRepository.findByRealmCodeAndWorkflowCode(realm, proc.getWorkflowCode()).orElse(null)
                : null;

        return ProcessBundleDto.builder()
                .realmCode(realm)
                .processKey(proc.getProcessKey())
                .name(proc.getName())
                .category(proc.getCategory())
                .industryType(proc.getIndustryType())
                .description(proc.getDescription())
                .tableName(proc.getTableName())
                .tableLabel(grid != null ? grid.getGridName() : null)
                .tableDescription(grid != null ? grid.getDescription() : null)
                .tableSchemaJson(proc.getTableSchemaJson() != null ? proc.getTableSchemaJson() : (grid != null ? grid.getColumnSchemaJson() : null))
                .form(form)
                .workflow(wf)
                .build();
    }

    @Transactional
    public ProcessInstance startProcess(ProcessStartRequest request) {
        if (request.getRealmCode() != null && !request.getRealmCode().isBlank()) {
            TenantContext.setTenantId(request.getRealmCode());
        }
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
            syncPhysicalTableRow(saved);
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
            syncPhysicalTableRow(saved);
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

    private void syncPhysicalTableRow(ProcessInstance instance) {
        try {
            ProcessDefinition proc = definitionRepository.findByRealmCodeAndProcessKey(
                    instance.getRealmCode(), instance.getProcessKey()
            ).orElse(null);
            if (proc != null && proc.getTableName() != null && !proc.getTableName().isBlank()) {
                String tableName = proc.getTableName();
                if (workflowTableMasterDao.tableExists(tableName) && instance.getPayloadJson() != null && !instance.getPayloadJson().isBlank()) {
                    Map<String, Object> map = objectMapper.readValue(instance.getPayloadJson(), new TypeReference<Map<String, Object>>() {});
                    map.put("process_tracking_no", instance.getTrackingNumber());
                    map.put("current_stage", instance.getCurrentStage());
                    map.put("created_by", instance.getInitiatedBy());
                    workflowRowRecordsDao.createRow(tableName, map);
                }
            }
        } catch (Exception e) {
            log.debug("[LOW-CODE SYNC] Physical table row sync notice: {}", e.getMessage());
        }
    }

    private WorkflowTableMaster buildWorkflowTableMaster(String tableName, String description, String schemaJson, List<String> columnNames) {
        List<WorkflowTableField> fields = new ArrayList<>();
        if (schemaJson != null && !schemaJson.isBlank()) {
            try {
                JsonNode arrayNode = objectMapper.readTree(schemaJson);
                if (arrayNode.isArray()) {
                    for (JsonNode colNode : arrayNode) {
                        String name = colNode.path("name").asText();
                        if (name == null || name.isBlank() || "id".equalsIgnoreCase(name)) continue;
                        String typeStr = colNode.path("type").asText("text").toLowerCase();
                        WorkflowTableFieldDataType dataType = switch (typeStr) {
                            case "number", "currency", "numeric" -> WorkflowTableFieldDataType.NUMERIC;
                            case "date", "datetime", "timestamp" -> WorkflowTableFieldDataType.TIMESTAMP;
                            case "boolean", "bool" -> WorkflowTableFieldDataType.BOOLEAN;
                            case "integer", "int" -> WorkflowTableFieldDataType.INTEGER;
                            case "textarea" -> WorkflowTableFieldDataType.TEXT;
                            default -> WorkflowTableFieldDataType.VARCHAR;
                        };
                        fields.add(WorkflowTableField.builder()
                                .fieldName(name)
                                .fieldDataType(dataType)
                                .size(255)
                                .fieldDesc(colNode.path("label").asText(name))
                                .build());
                    }
                }
            } catch (Exception e) {
                log.warn("[LOW-CODE DDL] Error parsing tableSchemaJson: {}", e.getMessage());
            }
        }
        if (fields.isEmpty() && columnNames != null) {
            for (String col : columnNames) {
                if (col == null || col.isBlank() || "id".equalsIgnoreCase(col)) continue;
                fields.add(WorkflowTableField.builder()
                        .fieldName(col)
                        .fieldDataType(WorkflowTableFieldDataType.VARCHAR)
                        .size(255)
                        .build());
            }
        }
        return WorkflowTableMaster.builder()
                .tableName(tableName)
                .tableDesc(description)
                .autoIncr(true)
                .virtualTableFieldsList(fields)
                .build();
    }
}
