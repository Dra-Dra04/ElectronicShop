package com.ecomelectronics.adminservice.service;

import com.ecomelectronics.adminservice.dto.CategoryDto;
import java.util.List;

public interface AdminCategoryService {
    List<CategoryDto> getAllCategories();
    CategoryDto createCategory(CategoryDto dto);
}
