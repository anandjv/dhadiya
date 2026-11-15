package com.dhadiya.flow.usms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "usms_supplier")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String realmCode;

    @Column(nullable = false, length = 150)
    private String supplierName;

    @Column(nullable = false, length = 60)
    private String supplierCode;

    @Column(length = 30)
    private String gstin;

    @Column(length = 50)
    private String phone;

    @Column(length = 120)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 60)
    private String city;

    @Column(length = 60)
    private String state;

    @Builder.Default
    private Integer creditDays = 30;

    @Builder.Default
    private BigDecimal outstandingBalance = BigDecimal.ZERO;

    @Builder.Default
    private Boolean isActive = true;

    private LocalDateTime createdAt;

    @PrePersist
    public void onPrePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (outstandingBalance == null) outstandingBalance = BigDecimal.ZERO;
    }
}
