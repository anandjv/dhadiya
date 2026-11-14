package com.dhadiya.tenants.controller;

import com.dhadiya.tenants.dto.TenantRealmRequest;
import com.dhadiya.tenants.dto.TenantRealmResponse;
import com.dhadiya.tenants.service.TenantRealmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/realms")
@RequiredArgsConstructor
@Tag(name = "Dhadiya Tenancy Realms API", description = "Multi-Tenant Realm and Organization Management for dhadiya.com")
public class TenantRealmController {

    private final TenantRealmService realmService;

    @GetMapping
    @Operation(summary = "Get all registered tenant realms")
    public ResponseEntity<List<TenantRealmResponse>> getAllRealms() {
        return ResponseEntity.ok(realmService.getAllRealms());
    }

    @GetMapping("/{realmCode}")
    @Operation(summary = "Get realm by unique code")
    public ResponseEntity<TenantRealmResponse> getRealmByCode(@PathVariable String realmCode) {
        return ResponseEntity.ok(realmService.getRealmByCode(realmCode));
    }

    @PostMapping
    @Operation(summary = "Provision a new tenant realm")
    public ResponseEntity<TenantRealmResponse> createRealm(@Valid @RequestBody TenantRealmRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(realmService.createRealm(request));
    }

    @PutMapping("/{realmCode}/settings")
    @Operation(summary = "Update realm configuration settings")
    public ResponseEntity<TenantRealmResponse> updateSettings(
            @PathVariable String realmCode,
            @RequestBody String settingsJson) {
        return ResponseEntity.ok(realmService.updateSettings(realmCode, settingsJson));
    }

    @PatchMapping("/{realmCode}/status")
    @Operation(summary = "Update realm operational status")
    public ResponseEntity<TenantRealmResponse> updateStatus(
            @PathVariable String realmCode,
            @RequestParam String status) {
        return ResponseEntity.ok(realmService.updateStatus(realmCode, status));
    }
}
