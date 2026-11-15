package com.dhadiya.flow.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantDB implements Serializable {
    private long id;
    private String driverName;
    private String url;
    private String userName;
    private String password;
    private String tenantId;
    private String mongoUri;
    private String logoUrl;
}
