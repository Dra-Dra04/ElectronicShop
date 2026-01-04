package com.ecomelectronics.adminservice.service;

import com.ecomelectronics.adminservice.Client.OrderClient;
import com.ecomelectronics.adminservice.Client.ProductClient;
import com.ecomelectronics.adminservice.Client.UserClient;
import com.ecomelectronics.adminservice.dto.DashboardOverviewDto;
import com.ecomelectronics.adminservice.dto.OrderStatsDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AdminDashboardService {

    private final ProductClient productClient;
    private final UserClient userClient;
    private final OrderClient orderClient;

    public AdminDashboardService(ProductClient productClient, UserClient userClient, OrderClient orderClient) {
        this.productClient = productClient;
        this.userClient = userClient;
        this.orderClient = orderClient;
    }

    public DashboardOverviewDto getOverview() {
        Long totalProducts = 0L;
        Long totalUsers = 0L;
        Long totalOrders = 0L;
        BigDecimal totalRevenue = BigDecimal.ZERO;

        try {
            totalProducts = productClient.countProducts();
        } catch (Exception e) {
            System.err.println("Lỗi Product Service: " + e.getMessage());
        }

        try {
            totalUsers = userClient.countUsers();
        } catch (Exception e) {
            System.err.println("Lỗi User Service: " + e.getMessage());
        }

        try {
            OrderStatsDto orderStats = orderClient.getOrderStats();
            if (orderStats != null) {
                totalOrders = orderStats.getCount();
                totalRevenue = orderStats.getRevenue();
            }
        } catch (Exception e) {
            System.err.println("Lỗi Order Service: " + e.getMessage());
        }

        return new DashboardOverviewDto(totalOrders, totalUsers, totalProducts, totalRevenue);
    }
}