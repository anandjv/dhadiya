package com.dhadiya.flow.controller;

import com.dhadiya.flow.dao.WorkflowRowRecordsDao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/workflow/records")
@RequiredArgsConstructor
@Tag(name = "Dynamic Row Records API", description = "Mirrors eFormsWorkflowDBAPI Dynamic Row Records CRUD")
public class WorkflowRowRecordsController {

    private final WorkflowRowRecordsDao workflowRowRecordsDao;

    @PostMapping("/{tableName}")
    @Operation(summary = "Insert row into dynamic table with RETURNING *")
    public ResponseEntity<Map<String, Object>> createRow(@PathVariable String tableName,
                                                         @RequestBody Map<String, Object> rowValues) {
        Map<String, Object> created = workflowRowRecordsDao.createRow(tableName, rowValues);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{tableName}/{id}")
    @Operation(summary = "Update row in dynamic table with RETURNING *")
    public ResponseEntity<Map<String, Object>> updateRow(@PathVariable String tableName,
                                                         @PathVariable Long id,
                                                         @RequestBody Map<String, Object> rowValues) {
        Map<String, Object> updated = workflowRowRecordsDao.updateRow(tableName, id, rowValues);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{tableName}/{id}")
    @Operation(summary = "Get row by ID from dynamic table")
    public ResponseEntity<Map<String, Object>> getRowById(@PathVariable String tableName,
                                                          @PathVariable Long id) {
        Map<String, Object> row = workflowRowRecordsDao.getRowById(tableName, id);
        if (row == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(row);
    }

    @GetMapping("/{tableName}")
    @Operation(summary = "Paginated query on dynamic table")
    public ResponseEntity<Map<String, Object>> getRows(@PathVariable String tableName,
                                                       @RequestParam(defaultValue = "50") int limit,
                                                       @RequestParam(defaultValue = "0") int offset,
                                                       @RequestParam(defaultValue = "id") String sortBy,
                                                       @RequestParam(defaultValue = "false") boolean ascending) {
        List<Map<String, Object>> rows = workflowRowRecordsDao.getRows(tableName, limit, offset, sortBy, ascending);
        long total = workflowRowRecordsDao.countRows(tableName);
        return ResponseEntity.ok(Map.of(
                "tableName", tableName,
                "total", total,
                "limit", limit,
                "offset", offset,
                "rows", rows
        ));
    }

    @DeleteMapping("/{tableName}/{id}")
    @Operation(summary = "Delete row from dynamic table by ID")
    public ResponseEntity<Map<String, Object>> deleteRow(@PathVariable String tableName,
                                                         @PathVariable Long id) {
        workflowRowRecordsDao.deleteRow(tableName, id);
        return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", "Row deleted successfully"));
    }
}
