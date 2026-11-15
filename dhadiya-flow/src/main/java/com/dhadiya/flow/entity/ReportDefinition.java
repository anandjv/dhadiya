package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dhadiya_report_def")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String realmCode;

    @Column(nullable = false, length = 100, unique = true)
    private String reportKey;

    @Column(nullable = false, length = 200)
    private String reportName;

    @Column(length = 60)
    private String category;

    @Column(length = 40)
    @Builder.Default
    private String chartType = "BAR";

    @Column(columnDefinition = "TEXT")
    private String queryConfigJson;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    private Boolean isPublic = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onPrePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
