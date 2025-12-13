package com.ecomelectronics.product_service.controller;

import com.ecomelectronics.product_service.model.Brand;
import com.ecomelectronics.product_service.repository.BrandRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@CrossOrigin(origins = "*")
public class BrandController {

    private final BrandRepository brandRepository;

    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @GetMapping
    public List<Brand> getAll() {
        return brandRepository.findAll();
    }

    @PostMapping
    public Brand create(@RequestBody Brand brand) {
        // khi gửi { "name": "Asus" } thì Jackson map vào field name
        return brandRepository.save(brand);
    }
}
