package com.ecomelectronics.controller;

import com.ecomelectronics.dto.CreateOrderRequest;
import com.ecomelectronics.dto.CreatePaymentResponse;
import com.ecomelectronics.model.Order;
import com.ecomelectronics.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(
           @Valid @RequestBody CreateOrderRequest request) {
        try {
            Order o = orderService.createOrder(request);
            
            // Nếu là thanh toán trực tuyến, lấy payment URL
            String paymentUrl = null;
            if (!"COD".equalsIgnoreCase(request.getPaymentMethod())) {
                try {
                    CreatePaymentResponse paymentResponse = orderService.getPaymentUrl(o.getId());
                    paymentUrl = paymentResponse.getPayUrl();
                } catch (Exception e) {
                    // Log nhưng không fail
                    System.err.println("Failed to get payment URL: " + e.getMessage());
                }
            }
            
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("id", o.getId());
            response.put("totalAmount", o.getTotalAmount());
            response.put("status", o.getStatus().name());
            if (paymentUrl != null) {
                response.put("paymentUrl", paymentUrl);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }
    
    /**
     * Lấy payment URL cho order (nếu là thanh toán trực tuyến)
     */
    @GetMapping("/{orderId}/payment-url")
    public ResponseEntity<?> getPaymentUrl(@PathVariable Long orderId) {
        try {
            CreatePaymentResponse response = orderService.getPaymentUrl(orderId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

