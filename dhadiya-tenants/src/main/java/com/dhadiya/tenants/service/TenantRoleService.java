package com.dhadiya.tenants.service;

import com.dhadiya.tenants.dto.TenantRoleRequest;
import com.dhadiya.tenants.dto.TenantRoleResponse;
import com.dhadiya.tenants.entity.TenantRole;
import com.dhadiya.tenants.repository.TenantRoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantRoleService {

    private final TenantRoleRepository roleRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public List<TenantRoleResponse> getAllRoles(String realmCode) {
        List<TenantRole> roles;
        if (realmCode != null && !realmCode.isBlank() && !realmCode.equalsIgnoreCase("all")) {
            roles = roleRepository.findByRealmCodeOrRealmCode(realmCode, "GLOBAL");
        } else {
            roles = roleRepository.findAll();
        }

        return roles.stream()
                .map(TenantRoleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TenantRoleResponse getRoleByCode(String roleCode) {
        return roleRepository.findByRoleCode(roleCode)
                .map(TenantRoleResponse::fromEntity)
                .orElseThrow(() -> new RuntimeException("Role not found with code: " + roleCode));
    }

    @Transactional
    public TenantRoleResponse createRole(TenantRoleRequest request) {
        if (roleRepository.existsByRoleCode(request.getRoleCode())) {
            throw new IllegalArgumentException("Role with code " + request.getRoleCode() + " already exists");
        }

        String permsJson = "[\"VIEW_DASHBOARD\"]";
        if (request.getPermissions() != null && !request.getPermissions().isEmpty()) {
            try {
                permsJson = objectMapper.writeValueAsString(request.getPermissions());
            } catch (JsonProcessingException e) {
                permsJson = "[\"VIEW_DASHBOARD\"]";
            }
        }

        TenantRole role = TenantRole.builder()
                .realmCode(request.getRealmCode())
                .roleCode(request.getRoleCode().toUpperCase().replaceAll("\\s+", "_"))
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .locationScope(request.getLocationScope() != null ? request.getLocationScope() : "ALL")
                .permissionsJson(permsJson)
                .colorCode(request.getColorCode() != null ? request.getColorCode() : "#0284c7")
                .isSystemRole(false)
                .build();

        return TenantRoleResponse.fromEntity(roleRepository.save(role));
    }

    @Transactional
    public void deleteRole(Long id) {
        TenantRole role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + id));
        if (role.isSystemRole()) {
            throw new IllegalStateException("System default roles cannot be deleted.");
        }
        roleRepository.delete(role);
    }
}
