package com.ecomelectronics.adminservice.controller;

import com.ecomelectronics.adminservice.dto.CategoryDto;
import com.ecomelectronics.adminservice.service.AdminCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@CrossOrigin(origins = "*")
public class AdminCategoryController {

    private final AdminCategoryService categoryService;

    public AdminCategoryController(AdminCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // GET /api/admin/categories -> loadCategories()
    @GetMapping
    public List<CategoryDto> getAll() {
        return categoryService.getAllCategories();
    }

    // POST /api/admin/categories -> createCategory()
    @PostMapping
    public CategoryDto create(@RequestBody CategoryDto dto) {
        return categoryService.createCategory(dto);
    }
}
