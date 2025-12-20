package com.ecomelectronics.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
@Data
public class OrderItemRequest {
    @NotNull
    private Long productId;
    @NotNull
    @Min(1)
    private Integer quantity;
}
