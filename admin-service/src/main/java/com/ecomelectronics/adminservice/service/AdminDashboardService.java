package com.ecomelectronics.adminservice.service;

import com.ecomelectronics.adminservice.dto.DashboardStatsDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AdminDashboardService {
    private final RestTemplate restTemplate;
    public AdminDashboardService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }
    public DashboardStatsDto getOverview(){
        DashboardStatsDto stats = new DashboardStatsDto();

        // Gọi sang Order-Service
        Long totalOrders = restTemplate.getForObject(
                "http://localhost:8083/api/orders/admin/count",
                Long.class
        );
        Double totalRevenue = restTemplate.getForObject(
                "http://localhost:8083/api/orders/admin/totalRevenue",
                Double.class
        );
        // Gọi sang Customer-Service
        Long totalUsers = restTemplate.getForObject(
                "http://localhost:8081/api/customers/admin/count",
                Long.class
        );

        // Gọi sang Product-Service
        Long totalProducts = restTemplate.getForObject(
                "http://localhost:8082/api/products/admin/count",
                Long.class
        );
        stats.setTotalOrders(totalOrders != null ? totalOrders : 0);
        stats.setTotalUsers(totalUsers != null ? totalUsers : 0);
        stats.setTotalProducts(totalProducts != null ? totalProducts : 0);
        stats.setTotalRevenue(totalRevenue != null ? totalRevenue : 0.0);

        return stats;

    }
}