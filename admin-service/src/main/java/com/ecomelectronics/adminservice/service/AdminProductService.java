package com.ecomelectronics.adminservice.service;

import com.ecomelectronics.adminservice.dto.ProductDto;
import java.util.List;

public interface AdminProductService {
    List<ProductDto> getAllProducts();
    ProductDto getProductById(Long id);
    ProductDto createProduct(ProductDto dto);
    void updateProduct(Long id, ProductDto dto);
    void deleteProduct(Long id);
}
