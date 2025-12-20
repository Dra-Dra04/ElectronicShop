package com.ecomelectronics.dto;

import com.ecomelectronics.model.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private Long userId;
    private List<OrderItemRequest> orderItems;
    private String shippingAddress;
    private String phone;
    private String paymentMethod ;
}
