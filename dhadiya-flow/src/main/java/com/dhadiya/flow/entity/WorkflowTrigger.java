package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dhadiya_wf_trigger")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTrigger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String triggerName;

    @Column(nullable = false, length = 100)
    private String sourceStageCode;

    @Column(nullable = false, length = 100)
    private String targetStageCode;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String actionName = "APPROVE";

    @Column(length = 50)
    @Builder.Default
    private String triggerType = "ACTION_CLICK"; // ACTION_CLICK, AUTO_EVALUATE, TIMEOUT

    @Column(columnDefinition = "TEXT")
    private String conditionExpression; // e.g. "severity == 'HIGH'"

    private String notificationEmail;
}
