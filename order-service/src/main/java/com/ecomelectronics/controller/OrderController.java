package com.ecomelectronics.controller;

import com.ecomelectronics.dto.CreateOrderRequest;
import com.ecomelectronics.model.Order;
import com.ecomelectronics.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(
           @Valid @RequestBody CreateOrderRequest request) {
        Order o = orderService.createOrder(request);
        return ResponseEntity.ok(java.util.Map.of(
                "id", o.getId(),
                "totalAmount", o.getTotalAmount(),
                "status", o.getStatus()
        ));
    }


    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }
}

