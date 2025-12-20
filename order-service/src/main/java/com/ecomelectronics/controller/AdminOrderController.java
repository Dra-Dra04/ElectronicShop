package com.ecomelectronics.controller;

import com.ecomelectronics.dto.OrderResponse;
import com.ecomelectronics.dto.OrderSummaryResponse;
import com.ecomelectronics.dto.UpdateOrderStatusRequest;
import com.ecomelectronics.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderSummaryResponse> list(@RequestParam(required = false) String status) {
        return orderService.adminList(status);
    }

    @GetMapping("/{orderId}")
    public OrderResponse detail(@PathVariable Long orderId) {
        return orderService.adminDetail(orderId);
    }
    
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest req) {

        return ResponseEntity.ok(
                orderService.updateOrderStatus(orderId, req.getStatus())
        );
    }

}

