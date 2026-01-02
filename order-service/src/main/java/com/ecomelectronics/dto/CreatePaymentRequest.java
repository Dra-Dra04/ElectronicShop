package com.ecomelectronics.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreatePaymentRequest {
    private Long orderId;
    private String method; // VNPAY, COD
    private BigDecimal amount;
}

