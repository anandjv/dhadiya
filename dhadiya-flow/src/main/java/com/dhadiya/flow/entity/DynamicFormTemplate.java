package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dhadiya_form_templates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DynamicFormTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String realmCode;

    @Column(nullable = false, length = 64)
    private String templateKey;

    @Column(nullable = false, length = 128)
    private String title;

    private Integer version;

    @Column(columnDefinition = "TEXT")
    private String schemaJson;

    @Column(length = 32)
    @Builder.Default
    private String status = "PUBLISHED";

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
