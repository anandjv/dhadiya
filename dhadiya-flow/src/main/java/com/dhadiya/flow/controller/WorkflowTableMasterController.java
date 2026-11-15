package com.dhadiya.flow.controller;

import com.dhadiya.flow.dao.WorkflowTableMasterDao;
import com.dhadiya.flow.model.WorkflowTableField;
import com.dhadiya.flow.model.WorkflowTableMaster;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/workflow/table")
@RequiredArgsConstructor
@Tag(name = "Dynamic DDL Table API", description = "Mirrors eFormsWorkflowDBAPI Dynamic Table Master Engine")
public class WorkflowTableMasterController {

    private final WorkflowTableMasterDao workflowTableMasterDao;

    @PostMapping("/create")
    @Operation(summary = "Dynamically execute CREATE TABLE DDL on routed tenant database")
    public ResponseEntity<Map<String, Object>> createTable(@RequestBody WorkflowTableMaster tableMaster) {
        workflowTableMasterDao.createTable(tableMaster);
        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "Dynamic table created successfully on routed tenant database",
                "tableName", tableMaster.getTableName()
        ));
    }

    @PostMapping("/{tableName}/add-field")
    @Operation(summary = "Dynamically execute ALTER TABLE ADD COLUMN on routed tenant database")
    public ResponseEntity<Map<String, Object>> addField(@PathVariable String tableName,
                                                        @RequestBody WorkflowTableField field) {
        workflowTableMasterDao.addField(tableName, field);
        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "Column added to dynamic table successfully",
                "tableName", tableName,
                "columnName", field.getFieldName()
        ));
    }

    @DeleteMapping("/{tableName}/remove-field/{fieldName}")
    @Operation(summary = "Dynamically execute ALTER TABLE DROP COLUMN on routed tenant database")
    public ResponseEntity<Map<String, Object>> removeField(@PathVariable String tableName,
                                                           @PathVariable String fieldName) {
        workflowTableMasterDao.removeField(tableName, fieldName);
        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "Column dropped from dynamic table successfully",
                "tableName", tableName,
                "columnName", fieldName
        ));
    }

    @DeleteMapping("/{tableName}")
    @Operation(summary = "Dynamically execute DROP TABLE on routed tenant database")
    public ResponseEntity<Map<String, Object>> deleteTable(@PathVariable String tableName) {
        workflowTableMasterDao.deleteTable(tableName);
        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "Dynamic table dropped successfully",
                "tableName", tableName
        ));
    }

    @GetMapping("/{tableName}/columns")
    @Operation(summary = "Get column list of dynamic table from information_schema")
    public ResponseEntity<List<String>> getColumns(@PathVariable String tableName) {
        return ResponseEntity.ok(workflowTableMasterDao.getColumns(tableName));
    }

    @GetMapping("/{tableName}/exists")
    @Operation(summary = "Check if dynamic table exists in routed tenant database")
    public ResponseEntity<Map<String, Object>> tableExists(@PathVariable String tableName) {
        boolean exists = workflowTableMasterDao.tableExists(tableName);
        return ResponseEntity.ok(Map.of("tableName", tableName, "exists", exists));
    }
}
