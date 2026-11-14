package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dhadiya_dyn_form_section")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String sectionKey;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false)
    private Integer sectionOrder;

    @Builder.Default
    private Integer columnLayout = 2; // 1, 2, 3, or 4 columns

    @Builder.Default
    private Boolean isRepeatable = false;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "form_section_id")
    @Builder.Default
    private List<FormComponent> components = new ArrayList<>();
}
