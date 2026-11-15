package com.dhadiya.flow.usms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "usms_stock_transfer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String realmCode;

    @Column(nullable = false, length = 60, unique = true)
    private String transferNumber;

    @Column(nullable = false, length = 60)
    private String fromBranchCode;

    @Column(nullable = false, length = 60)
    private String toBranchCode;

    private Long productId;
    private Integer quantity;

    private LocalDate transferDate;

    @Column(length = 40)
    @Builder.Default
    private String status = "IN_TRANSIT"; // DRAFT, IN_TRANSIT, RECEIVED, CANCELLED

    @Column(columnDefinition = "TEXT")
    private String notes;

    private LocalDateTime createdAt;

    @PrePersist
    public void onPrePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (transferDate == null) transferDate = LocalDate.now();
    }
}
