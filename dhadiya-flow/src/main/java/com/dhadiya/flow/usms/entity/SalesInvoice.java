package com.dhadiya.flow.usms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usms_sales_invoice")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String realmCode;

    @Column(nullable = false, length = 60, unique = true)
    private String invoiceNumber;

    @Column(nullable = false, length = 60)
    private String branchCode;

    private Long customerId;

    @Column(length = 150)
    private String customerName;

    @Column(length = 30)
    private String customerGstin;

    private LocalDate invoiceDate;

    @Column(length = 40)
    @Builder.Default
    private String invoiceType = "TAX_INVOICE"; // TAX_INVOICE, POS_BILL, ESTIMATE

    @Column(length = 40)
    @Builder.Default
    private String paymentMethod = "UPI"; // CASH, CARD, UPI, NETBANKING, CREDIT

    @Builder.Default
    private BigDecimal subTotal = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal cgstAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal sgstAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal igstAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal totalTax = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @Column(length = 40)
    @Builder.Default
    private String paymentStatus = "PAID"; // PAID, PARTIAL, UNPAID

    @Column(length = 100)
    private String salespersonName;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "salesInvoiceId")
    @Builder.Default
    private List<SalesInvoiceItem> items = new ArrayList<>();

    private LocalDateTime createdAt;

    @PrePersist
    public void onPrePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (invoiceDate == null) invoiceDate = LocalDate.now();
    }
}
