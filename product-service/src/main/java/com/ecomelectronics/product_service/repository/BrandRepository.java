package com.ecomelectronics.product_service.repository;

import com.ecomelectronics.product_service.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
