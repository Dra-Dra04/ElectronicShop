package com.ecomelectronics.adminservice.service;

import com.ecomelectronics.adminservice.dto.BrandDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class AdminBrandServiceImpl implements AdminBrandService {

    private final RestTemplate restTemplate;
    private final String productBaseUrl;

    public AdminBrandServiceImpl(
            RestTemplate restTemplate,
            @Value("${services.product-service.base-url}") String productBaseUrl
    ) {
        this.restTemplate = restTemplate;
        this.productBaseUrl = productBaseUrl;
    }

    @Override
    public List<BrandDto> getAllBrands() {
        // chỉnh path này cho đúng với BrandController bên product-service
        String url = productBaseUrl + "/api/brands";
        ResponseEntity<BrandDto[]> response =
                restTemplate.getForEntity(url, BrandDto[].class);
        BrandDto[] body = response.getBody();
        return body == null ? Collections.emptyList() : Arrays.asList(body);
    }

    @Override
    public BrandDto createBrand(BrandDto dto) {
        String url = productBaseUrl + "/api/brands";
        return restTemplate.postForObject(url, dto, BrandDto.class);
    }
}
