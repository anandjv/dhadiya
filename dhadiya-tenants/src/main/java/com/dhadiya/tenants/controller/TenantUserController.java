package com.dhadiya.tenants.controller;

import com.dhadiya.tenants.dto.TenantUserRequest;
import com.dhadiya.tenants.dto.TenantUserResponse;
import com.dhadiya.tenants.service.TenantUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Dhadiya Showroom Users API", description = "Multi-Tenant Staff & Role-Based Access Control for dhadiya.com Showrooms")
public class TenantUserController {

    private final TenantUserService userService;

    @GetMapping
    @Operation(summary = "List all showroom staff members, optionally filtered by realm")
    public ResponseEntity<List<TenantUserResponse>> getUsers(@RequestParam(required = false) String realmCode) {
        return ResponseEntity.ok(userService.getAllUsers(realmCode));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user details by ID")
    public ResponseEntity<TenantUserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    @Operation(summary = "Provision / Invite new showroom staff member")
    public ResponseEntity<TenantUserResponse> createUser(@Valid @RequestBody TenantUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update user status (ACTIVE, SUSPENDED, INVITED)")
    public ResponseEntity<TenantUserResponse> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String status = payload.getOrDefault("status", "ACTIVE");
        return ResponseEntity.ok(userService.updateUserStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove user access")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
