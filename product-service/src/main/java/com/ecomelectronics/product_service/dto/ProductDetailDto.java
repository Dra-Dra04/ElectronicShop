package com.ecomelectronics.product_service.dto;

import jakarta.validation.OverridesAttribute;

import java.math.BigDecimal;
import java.util.List;

public record ProductDetailDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Long categoryId,
        String categoryName,
        String brand,
        Integer stock,
        List<ProductImageDto> images) {
}
