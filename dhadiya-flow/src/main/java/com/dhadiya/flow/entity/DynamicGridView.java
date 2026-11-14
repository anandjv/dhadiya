package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dhadiya_dyn_grid_view")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DynamicGridView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String realmCode;

    @Column(nullable = false, length = 120)
    private String gridKey;

    @Column(nullable = false, length = 255)
    private String viewName;

    @Column(columnDefinition = "TEXT")
    private String filterSpecJson;

    @Column(columnDefinition = "TEXT")
    private String sortSpecJson;

    @Builder.Default
    private Boolean isDefault = false;

    private String createdBy;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (isDefault == null) isDefault = false;
    }
}
