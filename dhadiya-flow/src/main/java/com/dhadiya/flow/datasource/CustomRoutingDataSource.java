package com.dhadiya.flow.datasource;

import com.dhadiya.flow.config.TenantContext;
import com.dhadiya.flow.model.Constants;
import com.dhadiya.flow.service.TenantDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.sql.DataSource;

@Slf4j
public class CustomRoutingDataSource extends AbstractRoutingDataSource {

    private final TenantDBService tenantDBService;
    private final String defaultTenant;

    public CustomRoutingDataSource(TenantDBService tenantDBService, String defaultTenant) {
        this.tenantDBService = tenantDBService;
        this.defaultTenant = (defaultTenant != null && !defaultTenant.isBlank()) ? defaultTenant : "default";
    }

    @Override
    protected Object determineCurrentLookupKey() {
        // 1. ThreadLocal Context (highest priority, thread-safe)
        String ctxTenant = TenantContext.getTenantId();
        if (ctxTenant != null && !ctxTenant.isBlank()) {
            return ctxTenant;
        }

        // 2. RequestContextHolder attribute
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            Object obj = attr.getAttribute(Constants.TENANT_ID, RequestAttributes.SCOPE_REQUEST);
            if (obj != null && !obj.toString().isBlank()) {
                return obj.toString();
            }
        }

        return defaultTenant;
    }

    @Override
    protected DataSource determineTargetDataSource() {
        Object lookupKey = determineCurrentLookupKey();
        String tenantId = (lookupKey != null) ? lookupKey.toString() : defaultTenant;
        DataSource ds = tenantDBService.getDataSourceForTenant(tenantId);
        log.info("[TENANT ROUTING] Routing connection to isolated database for tenant: [{}]", tenantId);
        return ds;
    }
}
