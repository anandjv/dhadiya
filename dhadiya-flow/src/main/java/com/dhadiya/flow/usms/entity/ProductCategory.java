package com.dhadiya.flow.usms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usms_product_category")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String realmCode;

    @Column(nullable = false, length = 120)
    private String categoryName;

    @Column(nullable = false, length = 60)
    private String categoryCode;

    @Column(length = 60)
    private String industryType; // AUTOMOBILE, JEWELLERY, ELECTRONICS, FURNITURE, CLOTHING, MOBILE, HOME_APPLIANCES, HARDWARE, RETAIL

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    private Boolean isActive = true;

    private LocalDateTime createdAt;

    @PrePersist
    public void onPrePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
