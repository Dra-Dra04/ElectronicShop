package com.ecomelectronics.product_service.dto;

public record ProductImageDto(Long id,
                              String imageUrl,
                              Boolean isMain,
                              Integer sortOrder) {

}
