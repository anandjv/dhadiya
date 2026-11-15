package com.dhadiya.flow.service;

import com.dhadiya.flow.config.TenantContext;
import com.dhadiya.flow.dto.TenantLocationRequest;
import com.dhadiya.flow.dto.TenantLocationResponse;
import com.dhadiya.flow.entity.TenantLocation;
import com.dhadiya.flow.model.TenantDB;
import com.dhadiya.flow.repository.TenantLocationRepository;
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
public class TenantLocationService {

    private final TenantLocationRepository locationRepository;
    private final TenantDBService tenantDBService;

    @Transactional(readOnly = true)
    public List<TenantLocationResponse> getAllLocations(String realmCode) {
        if (realmCode != null && !realmCode.isBlank() && !realmCode.equalsIgnoreCase("all")) {
            TenantContext.setTenantId(realmCode);
            log.info("[TENANT LOCATIONS] Querying locations from tenant database for realm: [{}]", realmCode);
            return locationRepository.findAll().stream()
                    .map(TenantLocationResponse::fromEntity)
                    .collect(Collectors.toList());
        }

        // If 'all' or empty, aggregate across all provisioned tenant databases
        List<TenantLocationResponse> allLocations = new ArrayList<>();
        List<TenantDB> tenants = tenantDBService.getTenantDBList();
        for (TenantDB tenant : tenants) {
            try {
                TenantContext.setTenantId(tenant.getTenantId());
                List<TenantLocationResponse> tenantLocations = locationRepository.findAll().stream()
                        .map(TenantLocationResponse::fromEntity)
                        .collect(Collectors.toList());
                allLocations.addAll(tenantLocations);
            } catch (Exception e) {
                log.warn("[TENANT LOCATIONS] Could not query locations for tenant {}: {}", tenant.getTenantId(), e.getMessage());
            }
        }
        return allLocations;
    }

    @Transactional(readOnly = true)
    public TenantLocationResponse getLocationByCode(String locationCode, String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            TenantContext.setTenantId(realmCode);
        }
        return locationRepository.findByLocationCode(locationCode)
                .map(TenantLocationResponse::fromEntity)
                .orElseThrow(() -> new RuntimeException("Location not found for code: " + locationCode));
    }

    @Transactional
    public TenantLocationResponse createLocation(TenantLocationRequest request) {
        String realm = request.getRealmCode();
        if (realm == null || realm.isBlank()) {
            realm = TenantContext.getTenantId();
        }
        TenantContext.setTenantId(realm);

        log.info("[TENANT LOCATIONS] Provisioning location [{}] in tenant database: [{}]", request.getLocationCode(), realm);

        if (locationRepository.existsByLocationCode(request.getLocationCode())) {
            throw new IllegalArgumentException("Location with code " + request.getLocationCode() + " already exists in tenant database " + realm);
        }

        TenantLocation location = TenantLocation.builder()
                .realmCode(realm)
                .locationCode(request.getLocationCode())
                .locationName(request.getLocationName())
                .city(request.getCity())
                .stateProvince(request.getStateProvince())
                .country(request.getCountry())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .managerEmail(request.getManagerEmail())
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .build();

        return TenantLocationResponse.fromEntity(locationRepository.save(location));
    }

    @Transactional
    public void deleteLocation(Long id, String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            TenantContext.setTenantId(realmCode);
        }
        locationRepository.deleteById(id);
    }
}
