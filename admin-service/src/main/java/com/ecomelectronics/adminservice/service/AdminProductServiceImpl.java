package com.ecomelectronics.adminservice.service;

import com.ecomelectronics.adminservice.dto.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class AdminProductServiceImpl implements AdminProductService{

    private final RestTemplate restTemplate;
    private final String productBaseUrl;

    public AdminProductServiceImpl(
            RestTemplate restTemplate,
            @Value("${services.product-service.base-url}") String productBaseUrl
    ) {
        this.restTemplate = restTemplate;
        this.productBaseUrl = productBaseUrl;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        String url = productBaseUrl + "/api/products";
        ResponseEntity<ProductDto[]> response =
                restTemplate.getForEntity(url, ProductDto[].class);
        ProductDto[] body = response.getBody();
        return body == null ? Collections.emptyList() : Arrays.asList(body);
    }

    @Override
    public ProductDto getProductById(Long id) {
        String url = productBaseUrl + "/api/products/" + id;
        return restTemplate.getForObject(url, ProductDto.class);
    }

    @Override
    public ProductDto createProduct(ProductDto dto) {
        String url = productBaseUrl + "/api/products";
        return restTemplate.postForObject(url, dto, ProductDto.class);
    }

    @Override
    public void updateProduct(Long id, ProductDto dto) {
        String url = productBaseUrl + "/api/products/" + id;
        restTemplate.put(url, dto);
    }

    @Override
    public void deleteProduct(Long id) {
        String url = productBaseUrl + "/api/products/" + id;
        restTemplate.delete(url);
    }
}
