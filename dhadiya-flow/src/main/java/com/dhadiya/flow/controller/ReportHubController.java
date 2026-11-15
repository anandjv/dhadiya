package com.dhadiya.flow.controller;

import com.dhadiya.flow.entity.ReportDefinition;
import com.dhadiya.flow.service.ReportHubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Executive Intelligence & Report Hub", description = "Dynamic Reports, Analytics Dashboards & KPI Aggregations")
public class ReportHubController {

    private final ReportHubService reportService;

    @GetMapping("/definitions")
    @Operation(summary = "List all report definitions")
    public ResponseEntity<List<ReportDefinition>> getDefinitions(@RequestParam(required = false) String realmCode) {
        return ResponseEntity.ok(reportService.getReportDefinitions(realmCode));
    }

    @PostMapping("/definitions")
    @Operation(summary = "Create a custom report definition")
    public ResponseEntity<ReportDefinition> createDefinition(@RequestBody ReportDefinition def) {
        return ResponseEntity.ok(reportService.createReportDefinition(def));
    }

    @GetMapping("/analytics")
    @Operation(summary = "Get high-level executive analytics and KPI statistics")
    public ResponseEntity<Map<String, Object>> getAnalytics(@RequestParam(required = false) String realmCode) {
        return ResponseEntity.ok(reportService.getExecutiveAnalytics(realmCode));
    }

    @GetMapping("/run/{reportKey}")
    @Operation(summary = "Execute and view a report by key")
    public ResponseEntity<Map<String, Object>> runReport(
            @PathVariable String reportKey,
            @RequestParam(required = false) String realmCode) {
        return ResponseEntity.ok(reportService.runReport(reportKey, realmCode));
    }
}
