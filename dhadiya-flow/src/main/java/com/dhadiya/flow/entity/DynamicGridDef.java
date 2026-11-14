package com.dhadiya.flow.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dhadiya_dyn_grid_def")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DynamicGridDef {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String realmCode;

    @Column(nullable = false, unique = true, length = 120)
    private String gridKey;

    @Column(nullable = false)
    private String gridName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 120)
    private String processKey;

    @Builder.Default
    private Integer defaultPageSize = 20;

    @Builder.Default
    private Boolean enableExport = true;

    @Builder.Default
    private Boolean enabled = true;

    @Column(columnDefinition = "TEXT")
    private String columnSchemaJson;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonProperty("tableName")
    public String getTableName() {
        return this.gridKey;
    }

    @JsonProperty("label")
    public String getLabel() {
        return this.gridName;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (defaultPageSize == null) defaultPageSize = 20;
        if (enableExport == null) enableExport = true;
        if (enabled == null) enabled = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
