package com.ecomelectronics.adminservice.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "customer-service", url = "${application.config.customer-url}")
public interface UserClient {
    @GetMapping("/api/users/count") // Đảm bảo URL này khớp bên CustomerService
    Long countUsers();
}