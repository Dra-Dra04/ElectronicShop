package com.ecomelectronics.product_service.controller;

import com.ecomelectronics.product_service.dto.ProductDetailDto;
import com.ecomelectronics.product_service.dto.ProductDto;
import com.ecomelectronics.product_service.dto.ProductImageDto;
import com.ecomelectronics.product_service.model.Brand;
import com.ecomelectronics.product_service.model.Category;
import com.ecomelectronics.product_service.model.Product;
import com.ecomelectronics.product_service.model.ProductImage;
import com.ecomelectronics.product_service.repository.BrandRepository;
import com.ecomelectronics.product_service.repository.CategoryRepository;
import com.ecomelectronics.product_service.repository.ProductRepository;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final BrandRepository brandRepo;

    public ProductController(ProductRepository productRepo,
                             CategoryRepository categoryRepo,
                             BrandRepository brandRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.brandRepo = brandRepo;
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
    public List<String> getAllBrandNames() {
        return brandRepo.findAll()
                .stream()
                .map(Brand::getName)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
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
    @GetMapping("/api/products/{id}")
    public ResponseEntity<ProductDetailDto> getDetail(@PathVariable Long id) {
        Product p = productRepo.findByIdWithImages(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<ProductImageDto> imgs = p.getImages().stream()
                .sorted(Comparator.comparing(ProductImage::getSortOrder))
                .map(i -> new ProductImageDto(i.getId(), i.getImageUrl(), i.getIsMain(), i.getSortOrder()))
                .toList();

        ProductDetailDto dto = new ProductDetailDto(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(), // tuỳ kiểu price của bạn
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getBrand(),
                p.getStock(),
                imgs
        );

        return ResponseEntity.ok(dto);
    }

}
