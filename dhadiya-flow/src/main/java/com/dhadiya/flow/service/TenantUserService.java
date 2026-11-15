package com.dhadiya.flow.service;

import com.dhadiya.flow.config.TenantContext;
import com.dhadiya.flow.dto.TenantUserRequest;
import com.dhadiya.flow.dto.TenantUserResponse;
import com.dhadiya.flow.entity.TenantUser;
import com.dhadiya.flow.model.TenantDB;
import com.dhadiya.flow.repository.TenantUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantUserService {

    private final TenantUserRepository userRepository;
    private final TenantDBService tenantDBService;

    @Transactional(readOnly = true)
    public List<TenantUserResponse> getAllUsers(String realmCode) {
        if (realmCode != null && !realmCode.isBlank() && !realmCode.equalsIgnoreCase("all")) {
            TenantContext.setTenantId(realmCode);
            log.info("[TENANT USERS] Querying users from tenant database for realm: [{}]", realmCode);
            return userRepository.findAll().stream()
                    .map(TenantUserResponse::fromEntity)
                    .collect(Collectors.toList());
        }

        // If 'all' or empty, aggregate across all provisioned tenant databases
        List<TenantUserResponse> allUsers = new ArrayList<>();
        List<TenantDB> tenants = tenantDBService.getTenantDBList();
        for (TenantDB tenant : tenants) {
            try {
                TenantContext.setTenantId(tenant.getTenantId());
                List<TenantUserResponse> tenantUsers = userRepository.findAll().stream()
                        .map(TenantUserResponse::fromEntity)
                        .collect(Collectors.toList());
                allUsers.addAll(tenantUsers);
            } catch (Exception e) {
                log.warn("[TENANT USERS] Could not query users for tenant {}: {}", tenant.getTenantId(), e.getMessage());
            }
        }
        return allUsers;
    }

    @Transactional(readOnly = true)
    public TenantUserResponse getUserById(Long id, String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            TenantContext.setTenantId(realmCode);
        }
        return userRepository.findById(id)
                .map(TenantUserResponse::fromEntity)
                .orElseThrow(() -> new RuntimeException("Showroom user not found with ID: " + id));
    }

    @Transactional
    public TenantUserResponse createUser(TenantUserRequest request) {
        String realm = request.getRealmCode();
        if (realm == null || realm.isBlank()) {
            realm = TenantContext.getTenantId();
        }
        TenantContext.setTenantId(realm);

        log.info("[TENANT USERS] Provisioning user [{}] in tenant database: [{}]", request.getEmail(), realm);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with email " + request.getEmail() + " already exists in tenant database " + realm);
        }

        TenantUser user = TenantUser.builder()
                .realmCode(realm)
                .email(request.getEmail())
                .fullName(request.getFullName())
                .role(request.getRole())
                .locationCode(request.getLocationCode())
                .showroomLocation(request.getShowroomLocation())
                .phoneNumber(request.getPhoneNumber())
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .lastLoginAt(LocalDateTime.now())
                .build();

        return TenantUserResponse.fromEntity(userRepository.save(user));
    }

    @Transactional
    public TenantUserResponse updateUserStatus(Long id, String status, String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            TenantContext.setTenantId(realmCode);
        }
        TenantUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        user.setStatus(status);
        return TenantUserResponse.fromEntity(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id, String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            TenantContext.setTenantId(realmCode);
        }
        userRepository.deleteById(id);
    }
}
