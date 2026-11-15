package com.dhadiya.tenants.controller;

import com.dhadiya.tenants.dto.TenantDBDto;
import com.dhadiya.tenants.entity.Tenant;
import com.dhadiya.tenants.service.TenantMasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Master DB Data API", description = "Mirrors eFormsMaster DB Data & Tenant Routing API (from unified single tenants table)")
public class DBController {

    private final TenantMasterService tenantMasterService;

    @GetMapping("/db/tenant")
    @Operation(summary = "Core Dynamic Routing Endpoint: Get all tenant database connection credentials from single table for dhadiya-flow")
    public List<TenantDBDto> getAllTenantDBData() {
        return tenantMasterService.findAllTenantDB();
    }

    @PostMapping("/db")
    @Operation(summary = "Update DB configuration for tenant (stored directly in single tenants table)")
    public Tenant updateTenantDb(@RequestBody TenantDBDto dto) {
        return tenantMasterService.updateDbConfig(
                dto.getTenantId(),
                dto.getUrl(),
                dto.getUserName(),
                dto.getPassword(),
                dto.getDriverName(),
                dto.getMongoUri()
        );
    }

    @GetMapping("/db")
    @Operation(summary = "Get all DB configurations as DTOs")
    public List<TenantDBDto> getAllDBData() {
        return tenantMasterService.findAllTenantDB();
    }
}
