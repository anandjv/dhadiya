package com.dhadiya.flow.usms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usms_inventory_stock", uniqueConstraints = {
        @UniqueConstraint(name = "uk_realm_branch_product", columnNames = {"realmCode", "branchCode", "productId"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String realmCode;

    @Column(nullable = false, length = 60)
    private String branchCode;

    @Column(nullable = false)
    private Long productId;

    @Builder.Default
    private Integer currentStock = 0;

    @Builder.Default
    private Integer reservedStock = 0;

    @Builder.Default
    private Integer availableStock = 0;

    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    public void calculateAvailable() {
        if (currentStock == null) currentStock = 0;
        if (reservedStock == null) reservedStock = 0;
        this.availableStock = Math.max(0, this.currentStock - this.reservedStock);
        this.lastUpdated = LocalDateTime.now();
    }
}
