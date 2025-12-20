package com.ecomelectronics.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private BigDecimal amount;
    private String momoPaymentCode; // Mã thanh toán MoMo (format: MOMO-XXXX-XXXX)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
