package com.dhadiya.tenants.dto;

import com.dhadiya.tenants.entity.Tenant;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantDBDto implements Serializable {

    private long id;
    private String driverName;
    private String url;
    private String mongoUri;
    private String userName;
    private String password;
    private String tenantId;
    private String logoUrl;

    public TenantDBDto(Tenant tenant) {
        this.id = tenant.getId() != null ? tenant.getId() : 0L;
        this.driverName = tenant.getDriverName() != null ? tenant.getDriverName() : "org.postgresql.Driver";
        this.url = tenant.getUrl();
        this.mongoUri = tenant.getMongoUri();
        this.userName = tenant.getUserName();
        this.password = tenant.getPassword();
        this.tenantId = tenant.getTenantId();
        this.logoUrl = tenant.getLogoUrl();
    }
}
