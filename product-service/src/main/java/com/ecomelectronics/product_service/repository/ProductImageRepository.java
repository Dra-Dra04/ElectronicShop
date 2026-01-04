package com.ecomelectronics.product_service.repository;

import com.ecomelectronics.product_service.model.ProductImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProductIdOrderBySortOrderAsc(Long productId);
    @Transactional
    void deleteByProductId(Long productId);
}