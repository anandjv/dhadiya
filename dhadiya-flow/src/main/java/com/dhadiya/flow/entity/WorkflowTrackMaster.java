package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dhadiya_wf_track_master", indexes = {
        @Index(name = "idx_wtm_case", columnList = "caseNumber"),
        @Index(name = "idx_wtm_realm", columnList = "realmCode"),
        @Index(name = "idx_wtm_status", columnList = "status")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTrackMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String caseNumber;

    @Column(nullable = false, length = 60)
    private String realmCode;

    @Column(nullable = false, length = 120)
    private String workflowCode;

    private String workflowName;

    @Column(nullable = false, length = 100)
    private String currentStageCode;

    private String currentStageName;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String status = "IN_PROGRESS"; // IN_PROGRESS, COMPLETED, REJECTED, ON_HOLD

    private String priority;
    private String initiatedBy;

    private LocalDateTime initiatedOn;
    private LocalDateTime completedOn;
    private LocalDateTime lastUpdatedOn;

    private String tableName;

    @Column(columnDefinition = "TEXT")
    private String dataPayloadJson;

    @PrePersist
    protected void onCreate() {
        if (initiatedOn == null) initiatedOn = LocalDateTime.now();
        lastUpdatedOn = LocalDateTime.now();
        if (status == null) status = "IN_PROGRESS";
        if (priority == null) priority = "MEDIUM";
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedOn = LocalDateTime.now();
    }
}
