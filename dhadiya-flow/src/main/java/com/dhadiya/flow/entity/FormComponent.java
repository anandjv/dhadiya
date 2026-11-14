package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dhadiya_dyn_form_component")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fieldKey;

    @Column(nullable = false, length = 255)
    private String fieldLabel;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String componentType = "TEXT"; // TEXT, TEXTAREA, NUMBER, SELECT, RADIO, CHECKBOX, DATE, FILE, SIGNATURE, FORMULA

    private String placeholder;
    private String defaultValue;

    @Builder.Default
    private Boolean isRequired = false;

    @Builder.Default
    private Boolean isReadOnly = false;

    @Column(columnDefinition = "TEXT")
    private String optionsJson; // for SELECT, RADIO, CHECKBOX

    private String validationRegex;
    private Integer fieldOrder;
}
