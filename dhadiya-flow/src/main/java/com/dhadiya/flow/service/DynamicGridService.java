package com.dhadiya.flow.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dhadiya.flow.dto.*;
import com.dhadiya.flow.entity.DynamicGridDef;
import com.dhadiya.flow.entity.DynamicGridRecord;
import com.dhadiya.flow.entity.DynamicGridView;
import com.dhadiya.flow.entity.ProcessInstance;
import com.dhadiya.flow.repository.DynamicGridDefRepository;
import com.dhadiya.flow.repository.DynamicGridRecordRepository;
import com.dhadiya.flow.repository.DynamicGridViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DynamicGridService {

    private final DynamicGridDefRepository defRepository;
    private final DynamicGridRecordRepository recordRepository;
    private final DynamicGridViewRepository viewRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public List<DynamicGridDef> listGridDefinitions(String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            return defRepository.findByRealmCode(realmCode);
        }
        return defRepository.findAll();
    }

    @Transactional(readOnly = true)
    public DynamicGridDef getGridDefinition(String gridKey) {
        return defRepository.findByGridKey(gridKey)
                .orElseThrow(() -> new IllegalArgumentException("Grid definition not found: " + gridKey));
    }

    @Transactional
    public DynamicGridDef saveGridDefinition(GridDefinitionDto dto) {
        DynamicGridDef def = defRepository.findByGridKey(dto.getGridKey())
                .orElse(DynamicGridDef.builder()
                        .realmCode(dto.getRealmCode())
                        .gridKey(dto.getGridKey().trim().toLowerCase())
                        .build());

        def.setGridName(dto.getGridName());
        def.setDescription(dto.getDescription());
        def.setProcessKey(dto.getProcessKey());
        def.setColumnSchemaJson(dto.getColumnSchemaJson());
        if (dto.getDefaultPageSize() != null) {
            def.setDefaultPageSize(dto.getDefaultPageSize());
        }
        if (dto.getEnableExport() != null) {
            def.setEnableExport(dto.getEnableExport());
        }

        return defRepository.save(def);
    }

    @Transactional
    public DynamicGridRecord insertRecord(GridRecordDto dto) {
        DynamicGridRecord record = DynamicGridRecord.builder()
                .realmCode(dto.getRealmCode())
                .gridKey(dto.getGridKey().trim().toLowerCase())
                .processTrackingNumber(dto.getProcessTrackingNumber())
                .dataJson(dto.getDataJson())
                .build();

        return recordRepository.save(record);
    }

    @Transactional(readOnly = true)
    public GridDataResponseDto queryGrid(GridQueryDto query) {
        String realm = query.getRealmCode() != null ? query.getRealmCode() : "dhadiya-showroom-global";
        String gridKey = query.getGridKey();

        DynamicGridDef def = defRepository.findByGridKey(gridKey).orElse(null);
        String gridName = def != null ? def.getGridName() : gridKey;

        int pageNum = Math.max(0, query.getPage());
        int pageSize = query.getSize() > 0 ? query.getSize() : 20;

        List<DynamicGridRecord> allRawRecords = recordRepository.findByRealmCodeAndGridKey(realm, gridKey);

        Set<String> columnHeaders = new LinkedHashSet<>();
        List<Map<String, Object>> parsedRows = new ArrayList<>();

        for (DynamicGridRecord raw : allRawRecords) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("_id", raw.getId());
            if (raw.getProcessTrackingNumber() != null) {
                row.put("caseNumber", raw.getProcessTrackingNumber());
            }

            try {
                Map<String, Object> data = objectMapper.readValue(raw.getDataJson(), new TypeReference<>() {});
                row.putAll(data);
            } catch (Exception e) {
                row.put("rawPayload", raw.getDataJson());
            }

            // Filtering by search term across all values
            if (query.getSearch() != null && !query.getSearch().isBlank()) {
                String searchLower = query.getSearch().trim().toLowerCase();
                boolean matches = row.values().stream()
                        .filter(Objects::nonNull)
                        .anyMatch(v -> v.toString().toLowerCase().contains(searchLower));
                if (!matches) {
                    continue;
                }
            }

            // Filtering by specific column filters
            if (query.getFilter() != null && !query.getFilter().isEmpty()) {
                boolean matchesFilter = true;
                for (Map.Entry<String, Object> entry : query.getFilter().entrySet()) {
                    Object val = row.get(entry.getKey());
                    if (val == null || !val.toString().equalsIgnoreCase(entry.getValue().toString())) {
                        matchesFilter = false;
                        break;
                    }
                }
                if (!matchesFilter) {
                    continue;
                }
            }

            columnHeaders.addAll(row.keySet());
            parsedRows.add(row);
        }

        // Sorting if requested
        if (query.getSortBy() != null && !query.getSortBy().isBlank()) {
            String sortCol = query.getSortBy();
            boolean desc = "desc".equalsIgnoreCase(query.getSortDirection());
            parsedRows.sort((r1, r2) -> {
                Object v1 = r1.get(sortCol);
                Object v2 = r2.get(sortCol);
                if (v1 == null && v2 == null) return 0;
                if (v1 == null) return desc ? 1 : -1;
                if (v2 == null) return desc ? -1 : 1;
                int cmp = v1.toString().compareToIgnoreCase(v2.toString());
                return desc ? -cmp : cmp;
            });
        }

        long totalElements = parsedRows.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        int fromIndex = Math.min(pageNum * pageSize, parsedRows.size());
        int toIndex = Math.min(fromIndex + pageSize, parsedRows.size());
        List<Map<String, Object>> pagedRows = parsedRows.subList(fromIndex, toIndex);

        return GridDataResponseDto.builder()
                .gridKey(gridKey)
                .gridName(gridName)
                .realmCode(realm)
                .columns(new ArrayList<>(columnHeaders))
                .rows(pagedRows)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .page(pageNum)
                .size(pageSize)
                .build();
    }

    @Transactional(readOnly = true)
    public List<DynamicGridRecord> getRecordsByProcess(String trackingNumber) {
        return recordRepository.findByProcessTrackingNumber(trackingNumber);
    }

    @Transactional(readOnly = true)
    public String exportToCsv(String realmCode, String gridKey) {
        GridQueryDto query = GridQueryDto.builder()
                .realmCode(realmCode)
                .gridKey(gridKey)
                .page(0)
                .size(100000)
                .build();

        GridDataResponseDto data = queryGrid(query);
        StringBuilder sb = new StringBuilder();

        List<String> cols = data.getColumns();
        sb.append(String.join(",", cols)).append("\n");

        for (Map<String, Object> row : data.getRows()) {
            List<String> line = new ArrayList<>();
            for (String col : cols) {
                Object val = row.get(col);
                if (val == null) {
                    line.add("\"\"");
                } else {
                    String clean = val.toString().replace("\"", "\"\"");
                    line.add("\"" + clean + "\"");
                }
            }
            sb.append(String.join(",", line)).append("\n");
        }

        return sb.toString();
    }

    @Transactional(readOnly = true)
    public List<DynamicGridView> listViews(String realmCode, String gridKey) {
        return viewRepository.findByRealmCodeAndGridKey(realmCode, gridKey);
    }

    @Transactional
    public DynamicGridView saveView(GridViewDto dto) {
        DynamicGridView view = DynamicGridView.builder()
                .realmCode(dto.getRealmCode())
                .gridKey(dto.getGridKey())
                .viewName(dto.getViewName())
                .filterSpecJson(dto.getFilterSpecJson())
                .sortSpecJson(dto.getSortSpecJson())
                .isDefault(Boolean.TRUE.equals(dto.getIsDefault()))
                .createdBy(dto.getCreatedBy())
                .build();

        return viewRepository.save(view);
    }

    @Transactional
    public void syncFromProcessInstance(ProcessInstance instance) {
        if (instance.getProcessKey() == null) return;

        Optional<DynamicGridDef> defOpt = defRepository.findByProcessKey(instance.getProcessKey());
        if (defOpt.isEmpty()) {
            return;
        }

        DynamicGridDef def = defOpt.get();
        Optional<DynamicGridRecord> existing = recordRepository.findByProcessTrackingNumberAndGridKey(
                instance.getTrackingNumber(), def.getGridKey());

        if (existing.isPresent()) {
            DynamicGridRecord rec = existing.get();
            rec.setDataJson(instance.getPayloadJson() != null ? instance.getPayloadJson() : "{}");
            recordRepository.save(rec);
            log.info("Synced updated process instance {} to dynamic grid {}", instance.getTrackingNumber(), def.getGridKey());
        } else {
            DynamicGridRecord rec = DynamicGridRecord.builder()
                    .realmCode(instance.getRealmCode())
                    .gridKey(def.getGridKey())
                    .processTrackingNumber(instance.getTrackingNumber())
                    .dataJson(instance.getPayloadJson() != null ? instance.getPayloadJson() : "{}")
                    .build();
            recordRepository.save(rec);
            log.info("Indexed new process instance {} into dynamic grid {}", instance.getTrackingNumber(), def.getGridKey());
        }
    }
}
