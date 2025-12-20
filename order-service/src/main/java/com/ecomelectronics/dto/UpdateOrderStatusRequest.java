package com.ecomelectronics.dto;

import com.ecomelectronics.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class UpdateOrderStatusRequest {
    @NotNull
    private String status;
}
