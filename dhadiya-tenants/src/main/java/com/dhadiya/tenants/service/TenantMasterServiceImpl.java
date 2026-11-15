package com.dhadiya.tenants.service;

import com.dhadiya.tenants.dto.TenantDBDto;
import com.dhadiya.tenants.entity.Tenant;
import com.dhadiya.tenants.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("tenantMasterService")
@RequiredArgsConstructor
public class TenantMasterServiceImpl implements TenantMasterService {

    private final TenantRepository tenantRepository;

    @Override
    public List<Tenant> findAll() {
        return tenantRepository.findAll();
    }

    @Override
    public List<TenantDBDto> findAllTenantDB() {
        return tenantRepository.findAll().stream()
                .map(TenantDBDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public Tenant findById(Long id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + id));
    }

    @Override
    public Tenant findByTenantId(String tenantId) {
        return tenantRepository.findByTenantId(tenantId)
                .orElse(null);
    }

    @Override
    @Transactional
    public Tenant save(Tenant tenant) {
        if (tenant.getDriverName() == null || tenant.getDriverName().isBlank()) {
            tenant.setDriverName("org.postgresql.Driver");
        }
        return tenantRepository.save(tenant);
    }

    @Override
    @Transactional
    public Tenant update(Tenant tenant) {
        Tenant existing = findById(tenant.getId());
        existing.setTenantName(tenant.getTenantName());
        existing.setAddress(tenant.getAddress());
        existing.setCity(tenant.getCity());
        existing.setCountry(tenant.getCountry());
        existing.setZipCode(tenant.getZipCode());
        existing.setLogoUrl(tenant.getLogoUrl());
        existing.setStatus(tenant.getStatus());
        existing.setCustomDomain(tenant.getCustomDomain());
        existing.setSettingsJson(tenant.getSettingsJson());

        // Update embedded DB connection data if provided
        if (tenant.getUrl() != null) existing.setUrl(tenant.getUrl());
        if (tenant.getUserName() != null) existing.setUserName(tenant.getUserName());
        if (tenant.getPassword() != null) existing.setPassword(tenant.getPassword());
        if (tenant.getDriverName() != null) existing.setDriverName(tenant.getDriverName());
        if (tenant.getMongoUri() != null) existing.setMongoUri(tenant.getMongoUri());

        return tenantRepository.save(existing);
    }

    @Override
    @Transactional
    public Tenant updateDbConfig(String tenantId, String url, String userName, String password, String driverName, String mongoUri) {
        Tenant tenant = tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found with code: " + tenantId));
        if (url != null) tenant.setUrl(url);
        if (userName != null) tenant.setUserName(userName);
        if (password != null) tenant.setPassword(password);
        if (driverName != null) tenant.setDriverName(driverName);
        if (mongoUri != null) tenant.setMongoUri(mongoUri);
        return tenantRepository.save(tenant);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        tenantRepository.deleteById(id);
    }
}
