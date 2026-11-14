package com.dhadiya.tenants.controller;

import com.dhadiya.tenants.dto.TenantLocationRequest;
import com.dhadiya.tenants.dto.TenantLocationResponse;
import com.dhadiya.tenants.service.TenantLocationService;
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
@Tag(name = "Dhadiya Showroom Locations API", description = "Multi-Branch & Dealership Facility Management for Tenants")
public class TenantLocationController {

    private final TenantLocationService locationService;

    @GetMapping
    @Operation(summary = "List all showroom locations / dealership branches, optionally filtered by realm")
    public ResponseEntity<List<TenantLocationResponse>> getLocations(@RequestParam(required = false) String realmCode) {
        return ResponseEntity.ok(locationService.getAllLocations(realmCode));
    }

    @GetMapping("/{locationCode}")
    @Operation(summary = "Get location details by code")
    public ResponseEntity<TenantLocationResponse> getLocationByCode(@PathVariable String locationCode) {
        return ResponseEntity.ok(locationService.getLocationByCode(locationCode));
    }

    @PostMapping
    @Operation(summary = "Provision a new showroom branch / location")
    public ResponseEntity<TenantLocationResponse> createLocation(@Valid @RequestBody TenantLocationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationService.createLocation(request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Decommission location")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
