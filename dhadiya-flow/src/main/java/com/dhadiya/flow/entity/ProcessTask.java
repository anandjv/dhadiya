package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dhadiya_process_tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long instanceId;

    @Column(nullable = false, length = 64)
    private String trackingNumber;

    @Column(nullable = false, length = 64)
    private String stageName;

    @Column(nullable = false, length = 128)
    private String title;

    @Column(length = 64)
    private String assignee;

    @Column(nullable = false, length = 32)
    @Builder.Default
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED, COMPLETED

    @Column(length = 255)
    private String remarks;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime completedAt;
}
