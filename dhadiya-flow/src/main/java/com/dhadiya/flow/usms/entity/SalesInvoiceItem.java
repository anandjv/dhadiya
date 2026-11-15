package com.dhadiya.flow.usms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "usms_sales_invoice_item")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesInvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long salesInvoiceId;

    @Column(nullable = false)
    private Long productId;

    @Column(length = 200)
    private String productName;

    @Column(length = 100)
    private String serialOrImei;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal taxRate = BigDecimal.valueOf(18.0); // e.g. 18% GST

    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal netTotal = BigDecimal.ZERO;
}
