package com.dhadiya.flow.controller;

import com.dhadiya.flow.config.TenantContext;
import com.dhadiya.flow.dto.TenantUserRequest;
import com.dhadiya.flow.dto.TenantUserResponse;
import com.dhadiya.flow.service.TenantUserService;
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
@Tag(name = "Dhadiya Tenant Users API", description = "Multi-Tenant Staff & Role-Based Access Control Routed to Isolated Tenant DB")
public class TenantUserController {

    private final TenantUserService userService;

    @GetMapping
    @Operation(summary = "List showroom staff members from tenant database")
    public ResponseEntity<List<TenantUserResponse>> getUsers(@RequestParam(required = false) String realmCode) {
        if (realmCode != null && !realmCode.isBlank() && !realmCode.equalsIgnoreCase("all")) {
            TenantContext.setTenantId(realmCode);
        }
        return ResponseEntity.ok(userService.getAllUsers(realmCode));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user details by ID")
    public ResponseEntity<TenantUserResponse> getUserById(@PathVariable Long id, @RequestParam(required = false) String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            TenantContext.setTenantId(realmCode);
        }
        return ResponseEntity.ok(userService.getUserById(id, realmCode));
    }

    @PostMapping
    @Operation(summary = "Provision / Invite new staff member directly into tenant database")
    public ResponseEntity<TenantUserResponse> createUser(@Valid @RequestBody TenantUserRequest request,
                                                         @RequestParam(required = false) String realmCode) {
        String target = (realmCode != null && !realmCode.isBlank()) ? realmCode : request.getRealmCode();
        if (target != null && !target.isBlank()) {
            TenantContext.setTenantId(target);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update user status in tenant database")
    public ResponseEntity<TenantUserResponse> updateStatus(@PathVariable Long id,
                                                           @RequestBody Map<String, String> payload,
                                                           @RequestParam(required = false) String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            TenantContext.setTenantId(realmCode);
        }
        String status = payload.getOrDefault("status", "ACTIVE");
        return ResponseEntity.ok(userService.updateUserStatus(id, status, realmCode));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove user access from tenant database")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, @RequestParam(required = false) String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            TenantContext.setTenantId(realmCode);
        }
        userService.deleteUser(id, realmCode);
        return ResponseEntity.noContent().build();
    }
}
