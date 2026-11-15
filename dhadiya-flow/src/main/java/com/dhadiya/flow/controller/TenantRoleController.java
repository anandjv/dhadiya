package com.dhadiya.flow.controller;

import com.dhadiya.flow.config.TenantContext;
import com.dhadiya.flow.dto.TenantRoleRequest;
import com.dhadiya.flow.dto.TenantRoleResponse;
import com.dhadiya.flow.service.TenantRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Dhadiya Tenant Roles API", description = "Dynamic Role & Permission Management Scoped to Tenant Database")
public class TenantRoleController {

    private final TenantRoleService roleService;

    @GetMapping
    @Operation(summary = "List all roles from tenant database")
    public ResponseEntity<List<TenantRoleResponse>> getRoles(@RequestParam(required = false) String realmCode) {
        if (realmCode != null && !realmCode.isBlank() && !realmCode.equalsIgnoreCase("all")) {
            TenantContext.setTenantId(realmCode);
        }
        return ResponseEntity.ok(roleService.getAllRoles(realmCode));
    }

    @GetMapping("/{roleCode}")
    @Operation(summary = "Get role details by code from tenant database")
    public ResponseEntity<TenantRoleResponse> getRoleByCode(@PathVariable String roleCode,
                                                            @RequestParam(required = false) String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            TenantContext.setTenantId(realmCode);
        }
        return ResponseEntity.ok(roleService.getRoleByCode(roleCode, realmCode));
    }

    @PostMapping
    @Operation(summary = "Create custom dynamic role directly in tenant database")
    public ResponseEntity<TenantRoleResponse> createRole(@Valid @RequestBody TenantRoleRequest request,
                                                         @RequestParam(required = false) String realmCode) {
        String target = (realmCode != null && !realmCode.isBlank()) ? realmCode : request.getRealmCode();
        if (target != null && !target.isBlank()) {
            TenantContext.setTenantId(target);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete custom dynamic role from tenant database")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id, @RequestParam(required = false) String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            TenantContext.setTenantId(realmCode);
        }
        roleService.deleteRole(id, realmCode);
        return ResponseEntity.noContent().build();
    }
}
