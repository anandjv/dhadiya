package com.dhadiya.flow.service;

import com.dhadiya.flow.model.TenantDB;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service("tenantDBService")
public class TenantDBServiceImpl implements TenantDBService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${tenant.rest-url:http://localhost:8081/db/tenant}")
    private String tenantDBRestUrl;

    @Value("${tenant.default:default}")
    private String defaultTenant;

    @Value("${spring.datasource.url:jdbc:postgresql://localhost:5432/dhadiya_default}")
    private String fallbackDbUrl;

    @Value("${spring.datasource.username:postgres}")
    private String fallbackUser;

    @Value("${spring.datasource.password:postgres}")
    private String fallbackPass;

    @Value("${spring.datasource.driverClassName:org.postgresql.Driver}")
    private String fallbackDriver;

    private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();
    private DataSource defaultDataSource;

    @Override
    public List<TenantDB> getTenantDBList() {
        try {
            log.info("[TENANT ROUTING] Fetching tenant DBs from Master Service: {}", tenantDBRestUrl);
            ResponseEntity<TenantDB[]> response = restTemplate.getForEntity(tenantDBRestUrl, TenantDB[].class);
            if (response.getBody() != null) {
                return Arrays.asList(response.getBody());
            }
        } catch (Exception e) {
            log.warn("[TENANT ROUTING] Could not connect to Master Service at {} (will use fallback): {}",
                    tenantDBRestUrl, e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public synchronized Map<Object, Object> getDataSourceHashMap() {
        if (defaultDataSource == null) {
            defaultDataSource = createFallbackDataSource();
            dataSourceMap.put(defaultTenant, defaultDataSource);
        }

        List<TenantDB> list = getTenantDBList();
        for (TenantDB tenantDB : list) {
            if (tenantDB.getTenantId() != null && !dataSourceMap.containsKey(tenantDB.getTenantId())) {
                try {
                    DataSource ds = createDataSource(tenantDB);
                    dataSourceMap.put(tenantDB.getTenantId(), ds);
                    log.info("[TENANT ROUTING] Configured isolated DataSource for tenant [{}] -> {}",
                            tenantDB.getTenantId(), tenantDB.getUrl());
                } catch (Exception e) {
                    log.error("[TENANT ROUTING] Error configuring DataSource for tenant [{}]: {}",
                            tenantDB.getTenantId(), e.getMessage());
                }
            }
        }

        return new HashMap<>(dataSourceMap);
    }

    @Override
    public DataSource getDataSourceForTenant(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            return getDefaultDataSource();
        }

        DataSource ds = dataSourceMap.get(tenantId);
        if (ds != null) {
            return ds;
        }

        // Try dynamically fetching and refreshing from Master Service
        log.info("[TENANT ROUTING] Dynamic on-demand lookup for tenant [{}]...", tenantId);
        refreshDataSources();
        ds = dataSourceMap.get(tenantId);
        if (ds != null) {
            return ds;
        }

        log.warn("[TENANT ROUTING] No dedicated DataSource found for tenant [{}], falling back to default", tenantId);
        return getDefaultDataSource();
    }

    @Override
    public DataSource getDefaultDataSource() {
        if (defaultDataSource == null) {
            defaultDataSource = createFallbackDataSource();
            dataSourceMap.put(defaultTenant, defaultDataSource);
        }
        return defaultDataSource;
    }

    @Override
    public DataSource createDataSource(TenantDB tenantDB) {
        HikariConfig config = new HikariConfig();
        config.setPoolName("TenantPool-" + tenantDB.getTenantId());
        config.setDriverClassName(tenantDB.getDriverName() != null ? tenantDB.getDriverName() : fallbackDriver);
        config.setJdbcUrl(tenantDB.getUrl());
        config.setUsername(tenantDB.getUserName() != null ? tenantDB.getUserName() : fallbackUser);
        config.setPassword(tenantDB.getPassword() != null ? tenantDB.getPassword() : fallbackPass);
        config.setMaximumPoolSize(15);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(15000);
        config.setIdleTimeout(180000);
        config.setValidationTimeout(3000);
        return new HikariDataSource(config);
    }

    private DataSource createFallbackDataSource() {
        HikariConfig config = new HikariConfig();
        config.setPoolName("TenantPool-DefaultFallback");
        config.setDriverClassName(fallbackDriver);
        config.setJdbcUrl(fallbackDbUrl);
        config.setUsername(fallbackUser);
        config.setPassword(fallbackPass);
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        return new HikariDataSource(config);
    }

    @Override
    public synchronized void refreshDataSources() {
        List<TenantDB> list = getTenantDBList();
        for (TenantDB tenantDB : list) {
            if (tenantDB.getTenantId() != null && !dataSourceMap.containsKey(tenantDB.getTenantId())) {
                try {
                    DataSource ds = createDataSource(tenantDB);
                    dataSourceMap.put(tenantDB.getTenantId(), ds);
                    log.info("[TENANT ROUTING] Dynamically registered isolated DataSource for tenant [{}] -> {}",
                            tenantDB.getTenantId(), tenantDB.getUrl());
                } catch (Exception e) {
                    log.error("[TENANT ROUTING] Error dynamically adding DataSource for tenant [{}]: {}",
                            tenantDB.getTenantId(), e.getMessage());
                }
            }
        }
    }
}
