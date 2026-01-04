package com.ecomelectronics.adminservice.controller;

import com.ecomelectronics.adminservice.dto.BrandDto;
import com.ecomelectronics.adminservice.service.AdminBrandService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/brands")
//@CrossOrigin(origins = "*")
public class AdminBrandController {

    private final AdminBrandService brandService;

    public AdminBrandController(AdminBrandService brandService) {
        this.brandService = brandService;
    }

    // GET /api/admin/brands
    @GetMapping
    public List<BrandDto> getAll() {
        return brandService.getAllBrands();
    }

    // POST /api/admin/brands
    @PostMapping
    public BrandDto create(@RequestBody BrandDto dto) {
        return brandService.createBrand(dto);
    }
}
