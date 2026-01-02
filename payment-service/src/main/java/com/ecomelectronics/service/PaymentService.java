package com.ecomelectronics.service;

import com.ecomelectronics.dto.CreatePaymentRequest;
import com.ecomelectronics.dto.CreatePaymentResponse;
import com.ecomelectronics.dto.OrderResponse;
import com.ecomelectronics.dto.UpdateOrderStatusRequest;
import com.ecomelectronics.model.Payment;
import com.ecomelectronics.model.PaymentMethod;
import com.ecomelectronics.model.PaymentStatus;
import com.ecomelectronics.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;
    private final String orderServiceUrl;
    
    public PaymentService(
            PaymentRepository paymentRepository,
            RestTemplate restTemplate,
            @Value("${order.service.base-url}") String orderServiceUrl) {
        this.paymentRepository = paymentRepository;
        this.restTemplate = restTemplate;
        this.orderServiceUrl = orderServiceUrl;
    }
    
    // Lấy thông tin order từ Order Service
    public OrderResponse getOrderById(Long orderId) {
        String url = orderServiceUrl + "/api/admin/orders/" + orderId;
        try {
            return restTemplate.getForObject(url, OrderResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get order from Order Service: " + e.getMessage());
        }
    }
    

    public CreatePaymentResponse createPayment(CreatePaymentRequest request) {

        OrderResponse order = getOrderById(request.getOrderId());
        
        if (order == null) {
            throw new RuntimeException("Order not found: " + request.getOrderId());
        }
        

        if (request.getAmount().compareTo(order.getTotalAmount()) != 0) {
            throw new RuntimeException("Payment amount does not match order total");
        }
        

        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        
        try {
            payment.setMethod(PaymentMethod.valueOf(request.getMethod().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment method: " + request.getMethod());
        }
        
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        
        payment = paymentRepository.save(payment);

        String paymentUrl = null;
        if (PaymentMethod.VNPAY.equals(payment.getMethod())) {
            paymentUrl = "http://localhost:8086/api/payments/vnpay/create?paymentId=" + payment.getId();
        }

        CreatePaymentResponse response = new CreatePaymentResponse();
        response.setPaymentId(payment.getId());
        response.setStatus("PENDING");
        response.setPayUrl(paymentUrl);
        
        return response;
    }
    

    public Payment processPaymentSuccess(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
        

        if (payment.getStatus() == PaymentStatus.PAID) {
            return payment;
        }
        

        payment.setStatus(PaymentStatus.PAID);
        payment.setUpdatedAt(LocalDateTime.now());
        payment = paymentRepository.save(payment);
        

        try {
            updateOrderStatus(payment.getOrderId(), "PAID");
        } catch (Exception e) {
            System.err.println("Failed to update order status: " + e.getMessage());
        }
        
        return payment;
    }
    

    public Payment processPaymentFailure(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
        
        payment.setStatus(PaymentStatus.FAILED);
        payment.setUpdatedAt(LocalDateTime.now());
        payment = paymentRepository.save(payment);
        
        return payment;
    }

    public Payment cancelPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
        
        payment.setStatus(PaymentStatus.CANCELLED);
        payment.setUpdatedAt(LocalDateTime.now());
        payment = paymentRepository.save(payment);
        
        return payment;
    }

    private void updateOrderStatus(Long orderId, String status) {
        String url = orderServiceUrl + "/api/admin/orders/" + orderId + "/status";
        
        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
        request.setStatus(status);
        
        HttpEntity<UpdateOrderStatusRequest> entity = new HttpEntity<>(request);
        try {
            restTemplate.exchange(url, HttpMethod.PATCH, entity, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update order status: " + e.getMessage());
        }
    }
    

    public Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
    }
    
    // Lấy payment theo orderId
    public Payment getPaymentByOrderId(Long orderId) {
        return paymentRepository.findAll().stream()
                .filter(p -> p.getOrderId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Payment not found for order: " + orderId));
    }
}
