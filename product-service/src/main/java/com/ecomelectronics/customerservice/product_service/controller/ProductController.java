package com.ecomelectronics.customerservice.product_service.controller;

import com.ecomelectronics.customerservice.product_service.dto.ProductDto;
import com.ecomelectronics.customerservice.product_service.model.Category;
import com.ecomelectronics.customerservice.product_service.model.Product;
import com.ecomelectronics.customerservice.product_service.repository.CategoryRepository;
import com.ecomelectronics.customerservice.product_service.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public ProductController(ProductRepository productRepo,
                             CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    private ProductDto toDto(Product p) {
        ProductDto dto = new ProductDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setBrand(p.getBrand());
        dto.setImageUrl(p.getImageUrl());
        if (p.getCategory() != null) {
            dto.setCategoryId(p.getCategory().getId());
            dto.setCategoryName(p.getCategory().getName());
        }
        return dto;
    }

    @GetMapping
    public List<ProductDto> getAll(@RequestParam(required = false) Long categoryId,
                                   @RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) String brand) {

        List<Product> products;

        if (categoryId != null) {
            products = productRepo.findByCategory_Id(categoryId);
        } else if (brand != null && !brand.isEmpty()) {
            products = productRepo.findByBrandContainingIgnoreCase(brand);
        } else if (keyword != null && !keyword.isEmpty()) {
            products = productRepo.findByNameContainingIgnoreCase(keyword);
        } else {
            products = productRepo.findAll();
        }

        return products.stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        return productRepo.findById(id)
                .map(p -> ResponseEntity.ok(toDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/brands")
    public List<String> getBrands() {
        return productRepo.findDistinctBrands();
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto dto) {
        Product p = new Product();
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setBrand(dto.getBrand());
        p.setImageUrl(dto.getImageUrl());
        if (dto.getCategoryId() != null) {
            Category c = categoryRepo.findById(dto.getCategoryId()).orElse(null);
            p.setCategory(c);
        }
        Product saved = productRepo.save(p);
        return ResponseEntity.ok(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id,
                                             @RequestBody ProductDto dto) {
        return productRepo.findById(id)
                .map(p -> {
                    p.setName(dto.getName());
                    p.setDescription(dto.getDescription());
                    p.setPrice(dto.getPrice());
                    p.setBrand(dto.getBrand());
                    p.setImageUrl(dto.getImageUrl());
                    if (dto.getCategoryId() != null) {
                        Category c = categoryRepo.findById(dto.getCategoryId()).orElse(null);
                        p.setCategory(c);
                    }
                    return ResponseEntity.ok(toDto(productRepo.save(p)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!productRepo.existsById(id)) return ResponseEntity.notFound().build();
        productRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
