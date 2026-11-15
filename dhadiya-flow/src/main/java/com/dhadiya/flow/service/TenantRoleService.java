package com.dhadiya.flow.service;

import com.dhadiya.flow.config.TenantContext;
import com.dhadiya.flow.dto.TenantRoleRequest;
import com.dhadiya.flow.dto.TenantRoleResponse;
import com.dhadiya.flow.entity.TenantRole;
import com.dhadiya.flow.model.TenantDB;
import com.dhadiya.flow.repository.TenantRoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantRoleService {

    private final TenantRoleRepository roleRepository;
    private final TenantDBService tenantDBService;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public List<TenantRoleResponse> getAllRoles(String realmCode) {
        if (realmCode != null && !realmCode.isBlank() && !realmCode.equalsIgnoreCase("all")) {
            TenantContext.setTenantId(realmCode);
            log.info("[TENANT ROLES] Querying roles from tenant database for realm: [{}]", realmCode);
            return roleRepository.findAll().stream()
                    .map(TenantRoleResponse::fromEntity)
                    .collect(Collectors.toList());
        }

        List<TenantRoleResponse> allRoles = new ArrayList<>();
        List<TenantDB> tenants = tenantDBService.getTenantDBList();
        for (TenantDB tenant : tenants) {
            try {
                TenantContext.setTenantId(tenant.getTenantId());
                List<TenantRoleResponse> tenantRoles = roleRepository.findAll().stream()
                        .map(TenantRoleResponse::fromEntity)
                        .collect(Collectors.toList());
                allRoles.addAll(tenantRoles);
            } catch (Exception e) {
                log.warn("[TENANT ROLES] Could not query roles for tenant {}: {}", tenant.getTenantId(), e.getMessage());
            }
        }
        return allRoles;
    }

    @Transactional(readOnly = true)
    public TenantRoleResponse getRoleByCode(String roleCode, String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            TenantContext.setTenantId(realmCode);
        }
        return roleRepository.findByRoleCode(roleCode)
                .map(TenantRoleResponse::fromEntity)
                .orElseThrow(() -> new RuntimeException("Role not found with code: " + roleCode));
    }

    @Transactional
    public TenantRoleResponse createRole(TenantRoleRequest request) {
        String realm = request.getRealmCode();
        if (realm == null || realm.isBlank()) {
            realm = TenantContext.getTenantId();
        }
        TenantContext.setTenantId(realm);

        log.info("[TENANT ROLES] Provisioning role [{}] in tenant database: [{}]", request.getRoleCode(), realm);

        if (roleRepository.existsByRoleCode(request.getRoleCode())) {
            throw new IllegalArgumentException("Role with code " + request.getRoleCode() + " already exists in tenant database " + realm);
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
                .realmCode(realm)
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
    public void deleteRole(Long id, String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            TenantContext.setTenantId(realmCode);
        }
        roleRepository.deleteById(id);
    }
}
