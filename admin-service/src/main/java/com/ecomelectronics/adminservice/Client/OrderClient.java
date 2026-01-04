package com.ecomelectronics.adminservice.Client;

import com.ecomelectronics.adminservice.dto.OrderStatsDto; // Import DTO mà bạn đã copy sang admin-service
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "order-service", url = "${application.config.order-url}")
public interface OrderClient {
    @GetMapping("/api/orders/stats")
    OrderStatsDto getOrderStats();
}