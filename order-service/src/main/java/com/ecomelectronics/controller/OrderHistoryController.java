package com.ecomelectronics.controller;

import com.ecomelectronics.dto.OrderResponse;
import com.ecomelectronics.dto.OrderSummaryResponse;
import com.ecomelectronics.service.OrderQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderhis")
public class OrderHistoryController {
    private final OrderQueryService queryService;

    public OrderHistoryController(OrderQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/user/{userId}")
    public List<OrderSummaryResponse> history(@PathVariable Long userId) {
        return queryService.getOrderHistory(userId);
    }

    // Chi tiết: /api/orders/{orderId}?userId=...
    @GetMapping("/{orderId}")
    public OrderResponse detail(@PathVariable Long orderId, @RequestParam Long userId) {
        return queryService.getOrderDetail(orderId, userId);
    }

    // Hủy đơn:
    @PatchMapping("/{orderId}/cancel")
    public OrderSummaryResponse cancel(@PathVariable Long orderId, @RequestParam Long userId) {
        return queryService.cancelOrder(orderId, userId);
    }
}
