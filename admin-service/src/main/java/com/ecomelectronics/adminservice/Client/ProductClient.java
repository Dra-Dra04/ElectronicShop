package com.ecomelectronics.adminservice.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

// name: tên service đăng ký trên Eureka (hoặc tên định danh)
// url: lấy từ file cấu hình application.yml
@FeignClient(name = "product-service", url = "${application.config.product-url}")
public interface ProductClient {
    @GetMapping("/api/products/count")
    Long countProducts();
}
