package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dhadiya_process_instances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String trackingNumber;

    @Column(nullable = false, length = 64)
    private String realmCode;

    @Column(nullable = false, length = 64)
    private String processKey;

    @Column(nullable = false, length = 64)
    private String currentStage;

    @Column(nullable = false, length = 32)
    @Builder.Default
    private String status = "IN_PROGRESS"; // IN_PROGRESS, COMPLETED, REJECTED, TERMINATED

    @Column(length = 64)
    private String initiatedBy;

    @Column(columnDefinition = "TEXT")
    private String payloadJson;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime startedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;
}
