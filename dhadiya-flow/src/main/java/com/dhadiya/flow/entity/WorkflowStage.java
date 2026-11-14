package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dhadiya_wf_stage")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String stageCode;

    @Column(nullable = false, length = 255)
    private String stageName;

    @Column(nullable = false)
    private Integer stageOrder;

    @Column(length = 50)
    @Builder.Default
    private String stageType = "INTERMEDIATE"; // START, INTERMEDIATE, APPROVAL, REWORK, END

    @Builder.Default
    private Integer slaHours = 48;

    private String allowedRole;
    private String defaultAssignee;

    @Builder.Default
    private Boolean isMandatory = true;

    @Column(length = 500)
    private String actionsAllowed; // Comma-separated: "APPROVE, REJECT, REQUEST_CHANGES"

    @Column(length = 30)
    @Builder.Default
    private String stageColor = "#0284c7";
}
