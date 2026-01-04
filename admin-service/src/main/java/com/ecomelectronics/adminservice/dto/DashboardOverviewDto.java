package com.ecomelectronics.adminservice.dto; // Nhớ check package

import java.math.BigDecimal;

public class DashboardOverviewDto {
    private Long totalOrders;
    private Long totalUsers;
    private Long totalProducts;
    private BigDecimal totalRevenue;

    // 1. Constructor rỗng (Cần thiết để fix lỗi dòng 14)
    public DashboardOverviewDto() {
    }

    // 2. Constructor đầy đủ tham số (Cần thiết nếu dùng cách new Dto(a,b,c,d))
    public DashboardOverviewDto(Long totalOrders, Long totalUsers, Long totalProducts, BigDecimal totalRevenue) {
        this.totalOrders = totalOrders;
        this.totalUsers = totalUsers;
        this.totalProducts = totalProducts;
        this.totalRevenue = totalRevenue;
    }

    // 3. Getters & Setters (Cần thiết để fix lỗi dòng 36-39)
    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(Long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}