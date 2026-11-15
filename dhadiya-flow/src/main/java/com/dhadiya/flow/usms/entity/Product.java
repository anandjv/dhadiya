package com.dhadiya.flow.usms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "usms_product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String realmCode;

    @Column(nullable = false, length = 60)
    private String sku;

    @Column(nullable = false, length = 200)
    private String productName;

    private Long categoryId;
    private Long brandId;

    @Column(length = 60)
    private String industryType; // AUTOMOBILE, JEWELLERY, ELECTRONICS, FURNITURE, CLOTHING, MOBILE, HOME_APPLIANCES, HARDWARE, RETAIL

    @Column(length = 30)
    @Builder.Default
    private String unitOfMeasure = "NOS"; // NOS, PCS, GRAMS, SET, KG, METRE

    @Column(length = 30)
    private String hsnCode;

    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private BigDecimal mrp;

    @Builder.Default
    private Integer reorderLevel = 5;

    @Column(length = 100)
    private String barcode;

    @Column(columnDefinition = "TEXT")
    private String dynamicAttributesJson; // Industry-specific JSON: chassis, IMEI, purity, dimensions, size matrix, etc.

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    private Boolean isActive = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onPrePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
