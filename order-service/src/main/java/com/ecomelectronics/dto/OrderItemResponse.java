package com.ecomelectronics.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data

public class OrderItemResponse {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    public BigDecimal getLineTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
