package com.ecomelectronics.adminservice.service;

import com.ecomelectronics.adminservice.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final RestTemplate restTemplate;
    private final String productBaseUrl;

    public AdminCategoryServiceImpl(
            RestTemplate restTemplate,
            @Value("${services.product-service.base-url}") String productBaseUrl
    ) {
        this.restTemplate = restTemplate;
        this.productBaseUrl = productBaseUrl;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        String url = productBaseUrl + "/api/categories";
        ResponseEntity<CategoryDto[]> response =
                restTemplate.getForEntity(url, CategoryDto[].class);
        CategoryDto[] body = response.getBody();
        return body == null ? Collections.emptyList() : Arrays.asList(body);
    }

    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        String url = productBaseUrl + "/api/categories";
        return restTemplate.postForObject(url, dto, CategoryDto.class);
    }
}
