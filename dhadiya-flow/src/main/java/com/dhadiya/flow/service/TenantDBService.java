package com.dhadiya.flow.service;

import com.dhadiya.flow.model.TenantDB;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public interface TenantDBService {
    List<TenantDB> getTenantDBList();
    Map<Object, Object> getDataSourceHashMap();
    DataSource getDataSourceForTenant(String tenantId);
    DataSource createDataSource(TenantDB tenantDB);
    DataSource getDefaultDataSource();
    void refreshDataSources();
}
