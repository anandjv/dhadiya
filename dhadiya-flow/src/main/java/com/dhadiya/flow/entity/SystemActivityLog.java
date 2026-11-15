package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dhadiya_activity_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String realmCode;

    @Column(nullable = false, length = 150)
    private String userEmail;

    @Column(nullable = false, length = 60)
    private String actionType;

    @Column(length = 60)
    private String entityType;

    @Column(length = 60)
    private String entityId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(length = 60)
    private String ipAddress;

    private LocalDateTime timestamp;

    @PrePersist
    public void onPrePersist() {
        if (timestamp == null) timestamp = LocalDateTime.now();
    }
}
