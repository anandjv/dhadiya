package com.dhadiya.flow.controller;

import com.dhadiya.flow.entity.SystemActivityLog;
import com.dhadiya.flow.service.SystemActivityLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activity-logs")
@RequiredArgsConstructor
@Tag(name = "System Activity & Audit Trail", description = "Immutable audit log of user actions and state transitions")
public class SystemActivityLogController {

    private final SystemActivityLogService logService;

    @GetMapping
    @Operation(summary = "List recent system activity logs")
    public ResponseEntity<List<SystemActivityLog>> getLogs(@RequestParam(required = false) String realmCode) {
        return ResponseEntity.ok(logService.getLogs(realmCode));
    }

    @PostMapping
    @Operation(summary = "Record a new system activity log")
    public ResponseEntity<SystemActivityLog> recordLog(@RequestBody SystemActivityLog log) {
        return ResponseEntity.ok(logService.recordLog(log));
    }
}
