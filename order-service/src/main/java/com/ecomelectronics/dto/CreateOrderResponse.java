package com.ecomelectronics.dto;

import java.math.BigDecimal;
import lombok.Data;
@Data
public class CreateOrderResponse {
    private Long orderId;
    private BigDecimal totalAmount;
    private String status;
    private String paymentMethod;
    private String payUrl;
    private String requestId;
}
