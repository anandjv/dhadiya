package com.dhadiya.flow.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dhadiya_dyn_grid_record", indexes = {
        @Index(name = "idx_grid_rec_realm_key", columnList = "realmCode, gridKey"),
        @Index(name = "idx_grid_rec_tracking", columnList = "processTrackingNumber")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DynamicGridRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String realmCode;

    @Column(nullable = false, length = 120)
    private String gridKey;

    @Column(length = 100)
    private String processTrackingNumber;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String dataJson;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonProperty("tableName")
    public String getTableName() {
        return this.gridKey;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
