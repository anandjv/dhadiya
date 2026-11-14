package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dhadiya_wf_track_det", indexes = {
        @Index(name = "idx_wtd_case", columnList = "caseNumber")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTrackDet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String caseNumber;

    @Column(nullable = false, length = 100)
    private String stageCode;

    private String stageName;

    @Column(length = 100)
    private String actionTaken;

    private String actedBy;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    private LocalDateTime performedOn;
    private Long durationSeconds;

    private String previousStageCode;

    @Column(columnDefinition = "TEXT")
    private String snapshotDataJson;

    @PrePersist
    protected void onCreate() {
        if (performedOn == null) performedOn = LocalDateTime.now();
    }
}
