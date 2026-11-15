package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dhadiya_wf_task_item", indexes = {
        @Index(name = "idx_wt_case", columnList = "caseNumber"),
        @Index(name = "idx_wt_assigned", columnList = "assignedTo"),
        @Index(name = "idx_wt_status", columnList = "status")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTaskItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String caseNumber;

    @Column(nullable = false, length = 100)
    private String stageCode;

    @Column(nullable = false)
    private String taskTitle;

    private String assignedTo;
    private String roleName;

    @Column(length = 30)
    @Builder.Default
    private String priority = "NORMAL"; // URGENT, HIGH, NORMAL, LOW

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String status = "PENDING"; // PENDING, COMPLETED, OVERDUE

    private LocalDateTime createdAt;
    private LocalDateTime slaDueDate;
    private LocalDateTime completedAt;

    private String actionTaken;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = "PENDING";
    }
}
