package com.ecomelectronics.service;

import com.ecomelectronics.dto.OrderItemResponse;
import com.ecomelectronics.dto.OrderResponse;
import com.ecomelectronics.dto.OrderSummaryResponse;
import com.ecomelectronics.model.Order;
import com.ecomelectronics.model.OrderItem;
import com.ecomelectronics.repository.OrderItemRepository;
import com.ecomelectronics.repository.OrderRepository;
import org.springframework.stereotype.Service;
import com.ecomelectronics.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderQueryService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository itemRepo;

    public OrderQueryService(OrderRepository orderRepo, OrderItemRepository itemRepo) {
        this.orderRepo = orderRepo;
        this.itemRepo = itemRepo;
    }

    // 1) Lịch sử đơn hàng
    public List<OrderSummaryResponse> getOrderHistory(Long userId) {
        return orderRepo.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toSummary)
                .toList();
    }

    // 2) Chi tiết 1 đơn
    public OrderResponse getOrderDetail(Long orderId, Long userId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // bảo vệ: chỉ chủ đơn mới xem được
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }

        List<OrderItem> items = itemRepo.findByOrderId(orderId);
        return toDetail(order, items);
    }

    // 3) Hủy đơn (soft-cancel)
    public OrderSummaryResponse cancelOrder(Long orderId, Long userId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }

        // chỉ cho hủy khi CREATED
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new RuntimeException("Only CREATED orders can be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order saved = orderRepo.save(order);
        return toSummary(saved);
    }

    private OrderSummaryResponse toSummary(Order o) {
        OrderSummaryResponse dto = new OrderSummaryResponse();
        dto.setId(o.getId());
        dto.setStatus(o.getStatus().name());
        dto.setTotalAmount(o.getTotalAmount());
        dto.setCreatedAt(o.getCreatedAt());
        dto.setShippingAddress(o.getShippingAddress());
        return dto;
    }

    private OrderResponse toDetail(Order o, List<OrderItem> items) {
        OrderResponse dto = new OrderResponse();
        dto.setId(o.getId());
        dto.setUserId(o.getUserId());
        dto.setStatus(o.getStatus().name());
        dto.setTotalAmount(o.getTotalAmount());
        dto.setCreatedAt(o.getCreatedAt());
        dto.setShippingAddress(o.getShippingAddress());
        dto.setPhone(o.getPhone());
        dto.setPaymentMethod(o.getPaymentMethod());

        List<OrderItemResponse> itemDtos = items.stream().map(it -> {
            OrderItemResponse r = new OrderItemResponse();
            r.setProductId(it.getProductId());
            r.setProductName(it.getProductName());
            r.setPrice(it.getPrice());
            r.setQuantity(it.getQuantity());
            BigDecimal lineTotal = it.getPrice().multiply(BigDecimal.valueOf(it.getQuantity()));
            return r;
        }).toList();

        dto.setItems(itemDtos);
        return dto;
    }
}
