package com.dhadiya.flow;

import com.dhadiya.flow.datasource.CustomRoutingDataSource;
import com.dhadiya.flow.service.TenantDBService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.Map;

@SpringBootApplication
public class DhadiyaFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(DhadiyaFlowApplication.class, args);
    }

    @Bean
    @Primary
    public DataSource dataSource(TenantDBService tenantDBService,
                                 @Value("${tenant.default:default}") String defaultTenant) {
        CustomRoutingDataSource customDataSource = new CustomRoutingDataSource(tenantDBService, defaultTenant);
        Map<Object, Object> dsMap = tenantDBService.getDataSourceHashMap();
        customDataSource.setTargetDataSources(dsMap);
        customDataSource.setDefaultTargetDataSource(tenantDBService.getDefaultDataSource());
        customDataSource.afterPropertiesSet();
        return customDataSource;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
