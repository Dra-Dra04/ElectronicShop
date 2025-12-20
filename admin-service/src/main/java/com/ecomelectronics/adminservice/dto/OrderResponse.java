package com.ecomelectronics.adminservice.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private Long userId;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    private String shippingAddress;
    private String phone;
    private String paymentMethod;

    private List<OrderItemResponse> items;
}
