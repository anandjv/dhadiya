package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dhadiya_dyn_form_master")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String realmCode;

    @Column(nullable = false, unique = true, length = 120)
    private String formCode;

    @Column(nullable = false, length = 255)
    private String formName;

    @Column(columnDefinition = "TEXT")
    private String formDesc;

    private String tableName;
    private String category;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String status = "PUBLISHED"; // DRAFT, PUBLISHED, ARCHIVED

    @Builder.Default
    private Integer version = 1;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "form_master_id")
    @Builder.Default
    private List<FormSection> sections = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "form_master_id")
    @Builder.Default
    private List<FormRule> rules = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = "PUBLISHED";
        if (version == null) version = 1;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
