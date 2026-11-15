package com.dhadiya.flow.usms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usms_stock_transaction")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String realmCode;

    @Column(nullable = false, length = 60)
    private String branchCode;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false, length = 50)
    private String transactionType; // PURCHASE_INWARD, SALES_OUTWARD, TRANSFER_IN, TRANSFER_OUT, ADJUSTMENT

    @Column(nullable = false)
    private Integer quantity; // positive for inward, negative for outward

    private Integer balanceAfter;

    @Column(length = 100)
    private String referenceNumber; // Invoice #, PO #, Transfer #

    @Column(columnDefinition = "TEXT")
    private String notes;

    private LocalDateTime timestamp;

    @PrePersist
    public void onPrePersist() {
        if (timestamp == null) timestamp = LocalDateTime.now();
    }
}
