package com.ecomelectronics.model;

public enum PaymentStatus {
    PENDING,
    PAID, //đã thanh toán thành công
    FAILED, //thanh toán thất bại
    CANCELLED //hủy thanh toán
}
