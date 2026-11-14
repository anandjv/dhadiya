package com.dhadiya.flow.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dhadiya_dyn_form_rule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String ruleName;

    @Column(nullable = false, length = 100)
    private String triggerField;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String ruleType = "SHOW_HIDE"; // SHOW_HIDE, ENABLE_DISABLE, SET_REQUIRED, CALCULATE

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String conditionOperator = "EQUALS"; // EQUALS, NOT_EQUALS, GREATER_THAN, CONTAINS

    @Column(nullable = false)
    private String conditionValue;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String targetFieldsJson; // e.g. ["investigationLead", "capaPlan"]

    private String actionValue;
}
