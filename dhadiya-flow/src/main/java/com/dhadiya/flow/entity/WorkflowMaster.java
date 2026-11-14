package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dhadiya_wf_master")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String realmCode;

    @Column(nullable = false, unique = true, length = 120)
    private String workflowCode;

    @Column(nullable = false, length = 255)
    private String workflowName;

    @Column(columnDefinition = "TEXT")
    private String workflowDesc;

    private String category;
    private String tableName;
    private String formCode;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Integer version = 1;

    @Builder.Default
    private Boolean isCapa = false;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "workflow_id")
    @Builder.Default
    private List<WorkflowStage> stages = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "workflow_id")
    @Builder.Default
    private List<WorkflowTrigger> triggers = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (active == null) active = true;
        if (version == null) version = 1;
        if (isCapa == null) isCapa = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
