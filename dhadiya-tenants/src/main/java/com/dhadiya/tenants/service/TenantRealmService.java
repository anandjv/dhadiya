package com.dhadiya.tenants.service;

import com.dhadiya.tenants.dto.TenantRealmRequest;
import com.dhadiya.tenants.dto.TenantRealmResponse;
import com.dhadiya.tenants.entity.TenantRealm;
import com.dhadiya.tenants.repository.TenantRealmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantRealmService {

    private final TenantRealmRepository realmRepository;

    @Transactional(readOnly = true)
    public List<TenantRealmResponse> getAllRealms() {
        return realmRepository.findAll().stream()
                .map(TenantRealmResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TenantRealmResponse getRealmByCode(String realmCode) {
        TenantRealm realm = realmRepository.findByRealmCode(realmCode)
                .orElseThrow(() -> new IllegalArgumentException("Tenant realm not found: " + realmCode));
        return TenantRealmResponse.fromEntity(realm);
    }

    @Transactional
    public TenantRealmResponse createRealm(TenantRealmRequest request) {
        if (realmRepository.existsByRealmCode(request.getRealmCode())) {
            throw new IllegalArgumentException("Realm code already exists: " + request.getRealmCode());
        }

        TenantRealm realm = TenantRealm.builder()
                .realmCode(request.getRealmCode().trim().toLowerCase())
                .organizationName(request.getOrganizationName())
                .customDomain(request.getCustomDomain())
                .settingsJson(request.getSettingsJson())
                .status("ACTIVE")
                .build();

        return TenantRealmResponse.fromEntity(realmRepository.save(realm));
    }

    @Transactional
    public TenantRealmResponse updateSettings(String realmCode, String settingsJson) {
        TenantRealm realm = realmRepository.findByRealmCode(realmCode)
                .orElseThrow(() -> new IllegalArgumentException("Tenant realm not found: " + realmCode));
        realm.setSettingsJson(settingsJson);
        return TenantRealmResponse.fromEntity(realmRepository.save(realm));
    }

    @Transactional
    public TenantRealmResponse updateStatus(String realmCode, String status) {
        TenantRealm realm = realmRepository.findByRealmCode(realmCode)
                .orElseThrow(() -> new IllegalArgumentException("Tenant realm not found: " + realmCode));
        realm.setStatus(status);
        return TenantRealmResponse.fromEntity(realmRepository.save(realm));
    }
}
