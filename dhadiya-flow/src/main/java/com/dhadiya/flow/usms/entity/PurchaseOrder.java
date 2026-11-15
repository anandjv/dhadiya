package com.dhadiya.flow.usms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usms_purchase_order")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String realmCode;

    @Column(nullable = false, length = 60, unique = true)
    private String poNumber;

    @Column(nullable = false)
    private Long supplierId;

    @Column(length = 150)
    private String supplierName;

    @Column(nullable = false, length = 60)
    private String branchCode;

    private LocalDate poDate;

    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @Column(length = 40)
    @Builder.Default
    private String status = "APPROVED"; // DRAFT, APPROVED, RECEIVED, CANCELLED

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "purchaseOrderId")
    @Builder.Default
    private List<PurchaseOrderItem> items = new ArrayList<>();

    private LocalDateTime createdAt;

    @PrePersist
    public void onPrePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (poDate == null) poDate = LocalDate.now();
    }
}
