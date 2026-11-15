package com.dhadiya.flow.controller;

import com.dhadiya.flow.config.TenantContext;
import com.dhadiya.flow.dto.TenantLocationRequest;
import com.dhadiya.flow.dto.TenantLocationResponse;
import com.dhadiya.flow.service.TenantLocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
@Tag(name = "Dhadiya Tenant Locations API", description = "Multi-Branch & Dealership Facility Management in Tenant Database")
public class TenantLocationController {

    private final TenantLocationService locationService;

    @GetMapping
    @Operation(summary = "List showroom locations from tenant database")
    public ResponseEntity<List<TenantLocationResponse>> getLocations(@RequestParam(required = false) String realmCode) {
        if (realmCode != null && !realmCode.isBlank() && !realmCode.equalsIgnoreCase("all")) {
            TenantContext.setTenantId(realmCode);
        }
        return ResponseEntity.ok(locationService.getAllLocations(realmCode));
    }

    @GetMapping("/{locationCode}")
    @Operation(summary = "Get location details by code from tenant database")
    public ResponseEntity<TenantLocationResponse> getLocationByCode(@PathVariable String locationCode,
                                                                    @RequestParam(required = false) String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            TenantContext.setTenantId(realmCode);
        }
        return ResponseEntity.ok(locationService.getLocationByCode(locationCode, realmCode));
    }

    @PostMapping
    @Operation(summary = "Provision a new showroom branch directly into tenant database")
    public ResponseEntity<TenantLocationResponse> createLocation(@Valid @RequestBody TenantLocationRequest request,
                                                                 @RequestParam(required = false) String realmCode) {
        String target = (realmCode != null && !realmCode.isBlank()) ? realmCode : request.getRealmCode();
        if (target != null && !target.isBlank()) {
            TenantContext.setTenantId(target);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(locationService.createLocation(request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Decommission location from tenant database")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id, @RequestParam(required = false) String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            TenantContext.setTenantId(realmCode);
        }
        locationService.deleteLocation(id, realmCode);
        return ResponseEntity.noContent().build();
    }
}
