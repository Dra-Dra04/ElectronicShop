package com.ecomelectronics.model;

public enum OrderStatus {
    CREATED,     // khách vừa đặt
    PAID,        // khách đã thanh toán thành công
    APPROVED,    // admin duyệt
    SHIPPING,    // đang giao
    DELIVERED,   // đã giao
    CANCELLED    // khách hủy
}
