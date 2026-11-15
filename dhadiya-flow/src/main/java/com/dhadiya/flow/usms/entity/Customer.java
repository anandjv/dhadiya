package com.dhadiya.flow.usms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "usms_customer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String realmCode;

    @Column(nullable = false, length = 150)
    private String customerName;

    @Column(nullable = false, length = 50)
    private String phone;

    @Column(length = 120)
    private String email;

    @Column(length = 40)
    @Builder.Default
    private String customerType = "INDIVIDUAL"; // INDIVIDUAL, BUSINESS

    @Column(length = 30)
    private String gstin;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 60)
    private String city;

    @Column(length = 60)
    private String state;

    @Builder.Default
    private Integer loyaltyPoints = 0;

    @Builder.Default
    private BigDecimal outstandingBalance = BigDecimal.ZERO;

    @Builder.Default
    private Boolean isActive = true;

    private LocalDateTime createdAt;

    @PrePersist
    public void onPrePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (outstandingBalance == null) outstandingBalance = BigDecimal.ZERO;
        if (loyaltyPoints == null) loyaltyPoints = 0;
    }
}
