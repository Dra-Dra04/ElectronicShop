package com.ecomelectronics.adminservice.service;

import com.ecomelectronics.adminservice.dto.ProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class AdminProductService {

    private final RestTemplate restTemplate;

    // URL base của Product-Service
    private static final String PRODUCT_SERVICE_URL = "http://localhost:8081/api/products";

    public AdminProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ProductDto> getAllProducts() {
        ResponseEntity<ProductDto[]> response = restTemplate.getForEntity(
                PRODUCT_SERVICE_URL,
                ProductDto[].class
        );

        ProductDto[] body = response.getBody();
        if (body == null) {
            return List.of();
        }
        return Arrays.asList(body);
    }

    public ProductDto getProductById(Long id) {
        return restTemplate.getForObject(
                PRODUCT_SERVICE_URL + "/" + id,
                ProductDto.class
        );
    }

    public ProductDto createProduct(ProductDto dto) {
        return restTemplate.postForObject(
                PRODUCT_SERVICE_URL,
                dto,
                ProductDto.class
        );
    }

    public void updateProduct(Long id, ProductDto dto) {
        restTemplate.put(
                PRODUCT_SERVICE_URL + "/" + id,
                dto
        );
    }

    public void deleteProduct(Long id) {
        restTemplate.delete(PRODUCT_SERVICE_URL + "/" + id);
    }
}
