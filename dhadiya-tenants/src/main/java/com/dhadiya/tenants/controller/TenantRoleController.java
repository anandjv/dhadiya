package com.dhadiya.tenants.controller;

import com.dhadiya.tenants.dto.TenantRoleRequest;
import com.dhadiya.tenants.dto.TenantRoleResponse;
import com.dhadiya.tenants.service.TenantRoleService;
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
@Tag(name = "Dhadiya Dynamic Roles API", description = "Dynamic Role & Permission Management Scoped to Showroom Locations")
public class TenantRoleController {

    private final TenantRoleService roleService;

    @GetMapping
    @Operation(summary = "List all roles, optionally filtered by realm")
    public ResponseEntity<List<TenantRoleResponse>> getRoles(@RequestParam(required = false) String realmCode) {
        return ResponseEntity.ok(roleService.getAllRoles(realmCode));
    }

    @GetMapping("/{roleCode}")
    @Operation(summary = "Get role details by code")
    public ResponseEntity<TenantRoleResponse> getRoleByCode(@PathVariable String roleCode) {
        return ResponseEntity.ok(roleService.getRoleByCode(roleCode));
    }

    @PostMapping
    @Operation(summary = "Create custom dynamic role with location scoping & permissions")
    public ResponseEntity<TenantRoleResponse> createRole(@Valid @RequestBody TenantRoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete custom dynamic role")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
