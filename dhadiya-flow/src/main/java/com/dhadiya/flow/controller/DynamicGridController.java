package com.dhadiya.flow.controller;

import com.dhadiya.flow.dto.*;
import com.dhadiya.flow.entity.DynamicGridDef;
import com.dhadiya.flow.entity.DynamicGridRecord;
import com.dhadiya.flow.entity.DynamicGridView;
import com.dhadiya.flow.service.DynamicGridService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Dhadiya Flow - Dynamic DataGrid & Workflow Middleware API", description = "Unified middleware combining Dynamic Workflows and High-Speed DataGrids")
public class DynamicGridController {

    private final DynamicGridService gridService;

    // --- Definitions / Schemas / Catalog ---

    @GetMapping({"/api/v1/schemas", "/api/v1/grid/definitions", "/dynamicGrid/smart-grids"})
    @Operation(summary = "List all dynamic table & grid definitions")
    public ResponseEntity<List<DynamicGridDef>> listDefinitions(
            @RequestParam(required = false, defaultValue = "dhadiya-showroom-global") String realmCode) {
        return ResponseEntity.ok(gridService.listGridDefinitions(realmCode));
    }

    @GetMapping({"/api/v1/schemas/{gridKey}", "/api/v1/grid/definitions/{gridKey}", "/dynamicGrid/grids/{gridKey}/definition"})
    @Operation(summary = "Get dynamic table & grid definition and column schema")
    public ResponseEntity<DynamicGridDef> getDefinition(@PathVariable String gridKey) {
        return ResponseEntity.ok(gridService.getGridDefinition(gridKey));
    }

    @PostMapping({"/api/v1/schemas", "/api/v1/grid/definitions", "/dynamicGrid/admin/grids"})
    @Operation(summary = "Create or update dynamic table & grid definition")
    public ResponseEntity<DynamicGridDef> saveDefinition(@Valid @RequestBody GridDefinitionDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gridService.saveGridDefinition(dto));
    }

    // --- Querying & Data Retrieval ---

    @PostMapping({"/api/v1/grid/query", "/dynamicGrid/grids/{gridKey}/data"})
    @Operation(summary = "High-performance query dynamic grid with filters, search, and pagination")
    public ResponseEntity<GridDataResponseDto> queryGrid(
            @PathVariable(required = false) String gridKey,
            @RequestBody(required = false) GridQueryDto query) {

        if (query == null) {
            query = GridQueryDto.builder().build();
        }
        if (gridKey != null && !gridKey.isBlank()) {
            query.setGridKey(gridKey);
        }
        return ResponseEntity.ok(gridService.queryGrid(query));
    }

    @GetMapping("/api/v1/grid/records/{gridKey}")
    @Operation(summary = "Fetch paginated dynamic grid rows")
    public ResponseEntity<GridDataResponseDto> getRecords(
            @PathVariable String gridKey,
            @RequestParam(required = false, defaultValue = "dhadiya-showroom-global") String realmCode,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection) {

        GridQueryDto query = GridQueryDto.builder()
                .realmCode(realmCode)
                .gridKey(gridKey)
                .search(search)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        return ResponseEntity.ok(gridService.queryGrid(query));
    }

    @PostMapping("/api/v1/grid/records")
    @Operation(summary = "Insert dynamic row into grid")
    public ResponseEntity<DynamicGridRecord> insertRecord(@Valid @RequestBody GridRecordDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gridService.insertRecord(dto));
    }

    @GetMapping("/api/v1/grid/process/{trackingNumber}")
    @Operation(summary = "Query dynamic records linked to a workflow case tracking number")
    public ResponseEntity<List<DynamicGridRecord>> getByProcess(@PathVariable String trackingNumber) {
        return ResponseEntity.ok(gridService.getRecordsByProcess(trackingNumber));
    }

    // --- Export ---

    @GetMapping(value = {"/api/v1/grid/export/{gridKey}", "/dynamicGrid/grids/{gridKey}/export/excel", "/dynamicGrid/grids/{gridKey}/data/excel"})
    @Operation(summary = "Export dynamic grid records as CSV / Spreadsheet")
    public ResponseEntity<String> exportGrid(
            @PathVariable String gridKey,
            @RequestParam(required = false, defaultValue = "dhadiya-showroom-global") String realmCode) {

        String csv = gridService.exportToCsv(realmCode, gridKey);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + gridKey + ".csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }

    // --- Views ---

    @GetMapping({"/api/v1/grid/views/{gridKey}", "/dynamicGrid/grids/{gridKey}/views"})
    @Operation(summary = "List saved views for a grid")
    public ResponseEntity<List<DynamicGridView>> listViews(
            @PathVariable String gridKey,
            @RequestParam(required = false, defaultValue = "dhadiya-showroom-global") String realmCode) {
        return ResponseEntity.ok(gridService.listViews(realmCode, gridKey));
    }

    @PostMapping({"/api/v1/grid/views", "/dynamicGrid/grids/{gridKey}/views"})
    @Operation(summary = "Save custom grid filter view")
    public ResponseEntity<DynamicGridView> saveView(
            @PathVariable(required = false) String gridKey,
            @Valid @RequestBody GridViewDto dto) {

        if (gridKey != null && !gridKey.isBlank()) {
            dto.setGridKey(gridKey);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(gridService.saveView(dto));
    }
}
