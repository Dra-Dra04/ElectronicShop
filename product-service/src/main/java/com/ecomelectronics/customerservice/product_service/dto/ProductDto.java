package com.ecomelectronics.customerservice.product_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String brand;
    private String imageUrl;
    private Long categoryId;
    private String categoryName;
}
