package com.dhadiya.tenants.controller;

import com.dhadiya.tenants.entity.Tenant;
import com.dhadiya.tenants.service.TenantMasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Master Tenant API", description = "Mirrors eFormsMaster Tenant Management API")
public class TenantController {

    private final TenantMasterService tenantMasterService;

    @PostMapping("/tenant")
    @Operation(summary = "Create tenant")
    public Tenant createTenant(@RequestBody Tenant tenant) {
        return tenantMasterService.save(tenant);
    }

    @GetMapping("/tenant")
    @Operation(summary = "Get all tenants")
    public List<Tenant> getAllTenant() {
        return tenantMasterService.findAll();
    }

    @GetMapping("/tenant/{id}")
    @Operation(summary = "Get tenant by ID")
    public Tenant getTenant(@PathVariable Long id) {
        return tenantMasterService.findById(id);
    }

    @GetMapping("/tenant/by-code/{tenantId}")
    @Operation(summary = "Get tenant by Tenant ID Code")
    public Tenant getTenantByTenantId(@PathVariable String tenantId) {
        return tenantMasterService.findByTenantId(tenantId);
    }

    @PutMapping("/tenant")
    @Operation(summary = "Update tenant")
    public Tenant updateTenant(@RequestBody Tenant tenant) {
        return tenantMasterService.update(tenant);
    }

    @DeleteMapping("/tenant/{id}")
    @Operation(summary = "Delete tenant")
    public void removeTenant(@PathVariable Long id) {
        tenantMasterService.delete(id);
    }
}
