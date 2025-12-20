package com.ecomelectronics.adminservice.controller;

import com.ecomelectronics.adminservice.dto.OrderResponse;
import com.ecomelectronics.adminservice.dto.OrderSummaryResponse;
import com.ecomelectronics.adminservice.dto.UpdateOrderStatusRequest;
import com.ecomelectronics.adminservice.service.AdminOrderService;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
public class AdminOrderController {
    private final AdminOrderService adminOrderService;
    public AdminOrderController(AdminOrderService adminOrderService) {
        this.adminOrderService = adminOrderService;
    }

    @GetMapping
    public List<OrderSummaryResponse> list(
            @RequestParam(required = false) String status
    ) {
        return adminOrderService.list(status);
    }

    @GetMapping("/{orderId}")
    public OrderResponse detail(@PathVariable Long orderId) {
        return adminOrderService.detail(orderId);
    }

    @PatchMapping("/{orderId}/status")
    public OrderSummaryResponse updateStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest req
    ){
        return adminOrderService.updateStatus(orderId, req.getStatus());
    }

    @PatchMapping("/{orderId}/approve")
    public OrderSummaryResponse approve(@PathVariable Long orderId) {
        return adminOrderService.approve(orderId);
    }

    @PatchMapping("/{orderId}/ship")
    public OrderSummaryResponse ship(@PathVariable Long orderId) {
        return adminOrderService.ship(orderId);
    }

    @PatchMapping("/{orderId}/deliver")
    public OrderSummaryResponse deliver(@PathVariable Long orderId){
        return adminOrderService.deliver(orderId);
    }

    @PatchMapping("/{orderId}/cancel")
    public OrderSummaryResponse cancel(@PathVariable Long orderId){
        return adminOrderService.cancel(orderId);
    }

}
