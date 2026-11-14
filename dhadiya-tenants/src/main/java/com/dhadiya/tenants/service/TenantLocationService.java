package com.dhadiya.tenants.service;

import com.dhadiya.tenants.dto.TenantLocationRequest;
import com.dhadiya.tenants.dto.TenantLocationResponse;
import com.dhadiya.tenants.entity.TenantLocation;
import com.dhadiya.tenants.repository.TenantLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantLocationService {

    private final TenantLocationRepository locationRepository;

    @Transactional(readOnly = true)
    public List<TenantLocationResponse> getAllLocations(String realmCode) {
        List<TenantLocation> locations = (realmCode != null && !realmCode.isBlank() && !realmCode.equalsIgnoreCase("all"))
                ? locationRepository.findByRealmCode(realmCode)
                : locationRepository.findAll();

        return locations.stream()
                .map(TenantLocationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TenantLocationResponse getLocationByCode(String locationCode) {
        return locationRepository.findByLocationCode(locationCode)
                .map(TenantLocationResponse::fromEntity)
                .orElseThrow(() -> new RuntimeException("Location not found for code: " + locationCode));
    }

    @Transactional
    public TenantLocationResponse createLocation(TenantLocationRequest request) {
        if (locationRepository.existsByLocationCode(request.getLocationCode())) {
            throw new IllegalArgumentException("Location with code " + request.getLocationCode() + " already exists");
        }

        TenantLocation location = TenantLocation.builder()
                .realmCode(request.getRealmCode())
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
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }
}
