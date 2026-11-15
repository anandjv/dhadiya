package com.dhadiya.flow.service;

import com.dhadiya.flow.entity.ReportDefinition;
import com.dhadiya.flow.entity.WorkflowTrackMaster;
import com.dhadiya.flow.entity.WorkflowTaskItem;
import com.dhadiya.flow.repository.DynamicGridRecordRepository;
import com.dhadiya.flow.repository.ReportDefinitionRepository;
import com.dhadiya.flow.repository.WorkflowTaskItemRepository;
import com.dhadiya.flow.repository.WorkflowTrackMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportHubService {

    private final ReportDefinitionRepository reportDefRepository;
    private final WorkflowTrackMasterRepository trackMasterRepository;
    private final WorkflowTaskItemRepository taskItemRepository;
    private final DynamicGridRecordRepository gridRecordRepository;

    public List<ReportDefinition> getReportDefinitions(String realmCode) {
        return (realmCode != null && !realmCode.isBlank())
                ? reportDefRepository.findByRealmCode(realmCode)
                : reportDefRepository.findAll();
    }

    public ReportDefinition createReportDefinition(ReportDefinition def) {
        return reportDefRepository.save(def);
    }

    public Map<String, Object> getExecutiveAnalytics(String realmCode) {
        List<WorkflowTrackMaster> cases = (realmCode != null && !realmCode.isBlank())
                ? trackMasterRepository.findByRealmCode(realmCode)
                : trackMasterRepository.findAll();

        List<WorkflowTaskItem> allTasks = taskItemRepository.findAll();
        long openTasks = allTasks.stream().filter(t -> !"COMPLETED".equalsIgnoreCase(t.getStatus())).count();
        long completedTasks = allTasks.stream().filter(t -> "COMPLETED".equalsIgnoreCase(t.getStatus())).count();
        double slaRate = (openTasks + completedTasks > 0)
                ? ((double) (completedTasks) / (openTasks + completedTasks)) * 100.0
                : 100.0;

        long inventoryRecords = gridRecordRepository.count();

        // Stage breakdown
        Map<String, Long> stageBreakdown = cases.stream()
                .collect(Collectors.groupingBy(WorkflowTrackMaster::getCurrentStageCode, Collectors.counting()));

        // Priority breakdown of tasks
        Map<String, Long> priorityBreakdown = allTasks.stream()
                .collect(Collectors.groupingBy(WorkflowTaskItem::getPriority, Collectors.counting()));

        // Status breakdown
        Map<String, Long> statusBreakdown = cases.stream()
                .collect(Collectors.groupingBy(WorkflowTrackMaster::getStatus, Collectors.counting()));

        Map<String, Object> analytics = new LinkedHashMap<>();
        analytics.put("totalCases", cases.size());
        analytics.put("activeCases", cases.stream().filter(c -> "IN_PROGRESS".equalsIgnoreCase(c.getStatus())).count());
        analytics.put("completedCases", cases.stream().filter(c -> "COMPLETED".equalsIgnoreCase(c.getStatus())).count());
        analytics.put("openTasks", openTasks);
        analytics.put("totalInventoryRecords", inventoryRecords);
        analytics.put("slaCompliancePercentage", Math.round(slaRate * 10.0) / 10.0);
        analytics.put("stageBreakdown", stageBreakdown);
        analytics.put("priorityBreakdown", priorityBreakdown);
        analytics.put("statusBreakdown", statusBreakdown);

        return analytics;
    }

    public Map<String, Object> runReport(String reportKey, String realmCode) {
        Optional<ReportDefinition> defOpt = reportDefRepository.findByReportKey(reportKey);
        Map<String, Object> result = new LinkedHashMap<>();

        if (defOpt.isPresent()) {
            ReportDefinition def = defOpt.get();
            result.put("reportKey", def.getReportKey());
            result.put("reportName", def.getReportName());
            result.put("chartType", def.getChartType());
            result.put("category", def.getCategory());
        } else {
            result.put("reportKey", reportKey);
            result.put("reportName", "Standard Report: " + reportKey);
            result.put("chartType", "BAR");
        }

        result.put("generatedAt", new Date());
        result.put("analytics", getExecutiveAnalytics(realmCode));
        return result;
    }
}
