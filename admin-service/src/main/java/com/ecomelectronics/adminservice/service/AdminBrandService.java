package com.ecomelectronics.adminservice.service;

import com.ecomelectronics.adminservice.dto.BrandDto;
import java.util.List;

public interface AdminBrandService {
    List<BrandDto> getAllBrands();
    BrandDto createBrand(BrandDto dto);
}