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
import com.ecomelectronics.product_service.repository.ProductImageRepository;
import com.ecomelectronics.product_service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final BrandRepository brandRepo;
    private final ProductImageRepository imageRepo;

    public ProductController(ProductRepository productRepo,
                             CategoryRepository categoryRepo,
                             BrandRepository brandRepo, ProductImageRepository imageRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.brandRepo = brandRepo;
        this.imageRepo = imageRepo;
    }

    private ProductDto toDto(Product p) {
        ProductDto dto = new ProductDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setBrand(p.getBrand());
        dto.setStock(p.getStock());
        dto.setImageUrl(p.getImageUrl());
        if (p.getCategory() != null) {
            dto.setCategoryId(p.getCategory().getId());
            dto.setCategoryName(p.getCategory().getName());
        }
        return dto;
    }

    // --- HÀM LẤY DANH SÁCH SẢN PHẨM (Dùng cho cả Store và Admin Dashboard) ---
    // Khi Admin gọi /api/products mà không truyền tham số, nó sẽ chạy vào nhánh 'else' -> findAll()
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
            // Đây chính là dòng trả về toàn bộ sản phẩm cho Admin Dashboard
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
    @Transactional // Đảm bảo lưu Product và Images cùng thành công hoặc cùng thất bại
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto dto) {
        Product p = new Product();
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setStock(dto.getStock());
        p.setBrand(dto.getBrand());
        p.setImageUrl(dto.getImageUrl()); // Ảnh chính (nếu DB product có cột này)

        if (dto.getCategoryId() != null) {
            Category c = categoryRepo.findById(dto.getCategoryId()).orElse(null);
            p.setCategory(c);
        }

        // 1. Lưu Product trước để có ID
        Product savedProduct = productRepo.save(p);

        // 2. Xử lý lưu danh sách ảnh phụ vào bảng product_images
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            List<ProductImage> imageEntities = dto.getImages().stream().map(imgDto -> {
                ProductImage img = new ProductImage();
                img.setProduct(savedProduct); // Gán Product vừa lưu (đã có ID)
                img.setImageUrl(imgDto.getImageUrl());
                img.setIsMain(imgDto.getIsMain());
                img.setSortOrder(imgDto.getSortOrder());
                return img;
            }).collect(Collectors.toList());

            imageRepo.saveAll(imageEntities);
        }

        return ResponseEntity.ok(toDto(savedProduct));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ProductDto> update(@PathVariable Long id,
                                             @RequestBody ProductDto dto) {
        return productRepo.findById(id)
                .map(p -> {
                    p.setName(dto.getName());
                    p.setDescription(dto.getDescription());
                    p.setPrice(dto.getPrice());
                    p.setStock(dto.getStock());
                    p.setBrand(dto.getBrand());
                    p.setImageUrl(dto.getImageUrl());

                    if (dto.getCategoryId() != null) {
                        Category c = categoryRepo.findById(dto.getCategoryId()).orElse(null);
                        p.setCategory(c);
                    }
                    Product savedProduct = productRepo.save(p);

                    // --- CẬP NHẬT ẢNH ---
                    // Cách đơn giản nhất: Xóa hết ảnh cũ của sp này và lưu lại list mới từ Frontend
                    if (dto.getImages() != null) { // Chỉ update nếu frontend có gửi list images
                        imageRepo.deleteByProductId(savedProduct.getId()); // Xóa ảnh cũ

                        if (!dto.getImages().isEmpty()) {
                            List<ProductImage> imageEntities = dto.getImages().stream().map(imgDto -> {
                                ProductImage img = new ProductImage();
                                img.setProduct(savedProduct);
                                img.setImageUrl(imgDto.getImageUrl());
                                img.setIsMain(imgDto.getIsMain());
                                img.setSortOrder(imgDto.getSortOrder());
                                return img;
                            }).collect(Collectors.toList());
                            imageRepo.saveAll(imageEntities);
                        }
                    }

                    return ResponseEntity.ok(toDto(savedProduct));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!productRepo.existsById(id)) return ResponseEntity.notFound().build();
        productRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ProductDetailDto> getDetail(@PathVariable Long id) {
        // Hàm findByIdWithImages phải được viết trong ProductRepository dùng @Query JOIN FETCH
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
                p.getPrice(),
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getBrand(),
                p.getStock(),
                imgs // Trả về list ảnh
        );

        return ResponseEntity.ok(dto);
    }
    // --- API ĐẾM SỐ LƯỢNG (Dùng cho Dashboard) ---
    @GetMapping("/count")
    public long countProducts() {
        return productRepo.count();
    }
}