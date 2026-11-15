package com.dhadiya.tenants.service;

import com.dhadiya.tenants.dto.TenantDBDto;
import com.dhadiya.tenants.entity.Tenant;

import java.util.List;

public interface TenantMasterService {
    List<Tenant> findAll();
    List<TenantDBDto> findAllTenantDB();
    Tenant findById(Long id);
    Tenant findByTenantId(String tenantId);
    Tenant save(Tenant tenant);
    Tenant update(Tenant tenant);
    Tenant updateDbConfig(String tenantId, String url, String userName, String password, String driverName, String mongoUri);
    void delete(Long id);
}
