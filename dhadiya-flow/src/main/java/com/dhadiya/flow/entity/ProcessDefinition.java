package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dhadiya_process_definitions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String realmCode;

    @Column(nullable = false, length = 64)
    private String processKey;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(length = 255)
    private String description;

    private Integer version;

    @Column(columnDefinition = "TEXT")
    private String stagesJson;

    @Column(length = 64)
    private String defaultFormKey;

    @Column(length = 32)
    @Builder.Default
    private String status = "ACTIVE";

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
