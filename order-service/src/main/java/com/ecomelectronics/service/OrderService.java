package com.ecomelectronics.service;

import com.ecomelectronics.dto.*;
import com.ecomelectronics.model.Order;
import com.ecomelectronics.model.OrderItem;
import com.ecomelectronics.model.OrderStatus;
import com.ecomelectronics.repository.OrderItemRepository;
import com.ecomelectronics.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final OrderItemRepository itemRepo;
    private final RestTemplate restTemplate;
    private final String productServiceUrl;
    private final String paymentServiceUrl;

    public OrderService(OrderRepository orderRepo,
                       OrderItemRepository itemRepo,
                        RestTemplate restTemplate,
                        @Value("${product.service.url}") String productServiceUrl,
                        @Value("${payment.service.url}") String paymentServiceUrl){
        this.orderRepo = orderRepo;
        this.itemRepo = itemRepo;
        this.restTemplate = restTemplate;
        this.productServiceUrl = productServiceUrl;
        this.paymentServiceUrl = paymentServiceUrl;
    }
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }
    public Order createOrder(CreateOrderRequest req) {

        BigDecimal total = BigDecimal.ZERO;

        Order order = new Order();
        order.setUserId(req.getUserId());
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setShippingAddress(req.getShippingAddress());
        order.setPhone(req.getPhone());
        order.setPaymentMethod(req.getPaymentMethod());

        if ("COD".equalsIgnoreCase(req.getPaymentMethod())) {
            order.setStatus(OrderStatus.CREATED);
        } else {
            // Online: CHƯA trả tiền thì KHÔNG PAID
            order.setStatus(OrderStatus.CREATED);
        }
        order = orderRepo.save(order);

        for (OrderItemRequest i : req.getOrderItems()) {

            // 👉 GỌI PRODUCT SERVICE
            ProductResponse p = callProductService(i.getProductId());

            BigDecimal itemTotal =
                    p.getPrice().multiply(BigDecimal.valueOf(i.getQuantity()));

            total = total.add(itemTotal);

            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(p.getId());
            item.setProductName(p.getName());
            item.setPrice(p.getPrice());
            item.setQuantity(i.getQuantity());

            itemRepo.save(item);
        }

        order.setTotalAmount(total);
        order = orderRepo.save(order);

        // Nếu là thanh toán trực tuyến, gọi Payment Service để tạo payment
        if (!"COD".equalsIgnoreCase(req.getPaymentMethod())) {
            try {
                CreatePaymentRequest paymentRequest = new CreatePaymentRequest();
                paymentRequest.setOrderId(order.getId());
                paymentRequest.setMethod(req.getPaymentMethod());
                paymentRequest.setAmount(total);
                
                // Gọi Payment Service để tạo payment
                CreatePaymentResponse paymentResponse = restTemplate.postForObject(
                    paymentServiceUrl + "/api/payments/create",
                    paymentRequest,
                    CreatePaymentResponse.class
                );
                
                // Payment đã được tạo thành công
                // paymentResponse chứa paymentId và paymentUrl nếu cần
            } catch (Exception e) {
                // Log error nhưng không fail order creation
                System.err.println("Failed to create payment: " + e.getMessage());
            }
        }

        return order;
    }
    
    // Method để lấy payment URL cho order
    public CreatePaymentResponse getPaymentUrl(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if ("COD".equalsIgnoreCase(order.getPaymentMethod())) {
            throw new RuntimeException("Order is COD, no payment URL needed");
        }
        
        CreatePaymentRequest paymentRequest = new CreatePaymentRequest();
        paymentRequest.setOrderId(orderId);
        paymentRequest.setMethod(order.getPaymentMethod());
        paymentRequest.setAmount(order.getTotalAmount());
        
        return restTemplate.postForObject(
            paymentServiceUrl + "/api/payments/create",
            paymentRequest,
            CreatePaymentResponse.class
        );
    }


    private ProductResponse callProductService(Long productId) {
        return restTemplate.getForObject(
                productServiceUrl + "/api/products/" + productId,
                ProductResponse.class
        );
    }

    public Order updateOrderStatus(Long orderId, String newStatusStr) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus cur = order.getStatus();

        if (newStatusStr == null || newStatusStr.isBlank()) {
            throw new RuntimeException("Status is required");
        }

        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(newStatusStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + newStatusStr);
        }

        // Không cho đổi nếu đã kết thúc
        if (cur == OrderStatus.CANCELLED || cur == OrderStatus.DELIVERED) {
            throw new RuntimeException("Order is closed. Can't change status.");
        }

        // Validate luật chuyển trạng thái
        boolean ok =
                (cur == OrderStatus.CREATED   && (newStatus == OrderStatus.APPROVED || newStatus == OrderStatus.CANCELLED || newStatus == OrderStatus.PAID)) ||
                        (cur == OrderStatus.PAID      && (newStatus == OrderStatus.APPROVED || newStatus == OrderStatus.CANCELLED)) ||
                        (cur == OrderStatus.APPROVED  && (newStatus == OrderStatus.SHIPPING)) ||
                        (cur == OrderStatus.SHIPPING  && (newStatus == OrderStatus.DELIVERED));

        if (!ok) {
            throw new RuntimeException("Invalid status transition: " + cur + " -> " + newStatus);
        }

        order.setStatus(newStatus);
        return orderRepo.save(order);
    }


    public Order cancelOrder(Long orderId, Long userId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Không có quyền hủy đơn này");
        }

        if (order.getStatus() != OrderStatus.CREATED &&
                order.getStatus() != OrderStatus.APPROVED) {
            throw new RuntimeException("Không thể hủy đơn ở trạng thái này");
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepo.save(order);
    }

    public List<OrderSummaryResponse> adminList(String status) {
        List<Order> orders;
        if (status == null || status.isBlank()) {
            orders = orderRepo.findAll();
        } else {
            OrderStatus st = OrderStatus.valueOf(status.toUpperCase());
            orders = orderRepo.findByStatus(st);
        }
        return orders.stream().map(this::toSummaryDto).toList();
    }


    public OrderResponse adminDetail(Long orderId) {
        Order o = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        return toDetailDto(o);
    }

    private OrderSummaryResponse toSummaryDto(Order o) {
        OrderSummaryResponse dto = new OrderSummaryResponse();
        dto.setId(o.getId());
        dto.setStatus(o.getStatus() != null ? o.getStatus().name() : null);
        dto.setTotalAmount(o.getTotalAmount());
        dto.setCreatedAt(o.getCreatedAt());
        dto.setShippingAddress(o.getShippingAddress());
        return dto;
    }

    private OrderResponse toDetailDto(Order o) {
        OrderResponse dto = new OrderResponse();
        dto.setId(o.getId());
        dto.setUserId(o.getUserId());
        dto.setStatus(o.getStatus() != null ? o.getStatus().name() : null);
        dto.setTotalAmount(o.getTotalAmount());
        dto.setCreatedAt(o.getCreatedAt());
        dto.setShippingAddress(o.getShippingAddress());
        dto.setPhone(o.getPhone());
        dto.setPaymentMethod(o.getPaymentMethod());

        // lấy items từ OrderItemRepository vì Order entity chưa có List<OrderItem>
        List<OrderItem> items = itemRepo.findByOrderId(o.getId());
        dto.setItems(items.stream().map(it -> {
            OrderItemResponse r = new OrderItemResponse();
            r.setProductId(it.getProductId());
            r.setProductName(it.getProductName());
            r.setPrice(it.getPrice());
            r.setQuantity(it.getQuantity());
            return r;
        }).toList());

        return dto;
    }



}
