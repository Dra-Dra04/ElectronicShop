package com.ecomelectronics.adminservice.controller;

import com.ecomelectronics.adminservice.dto.ProductDto;
import com.ecomelectronics.adminservice.service.AdminProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin(origins = "*")
public class AdminProductController {

    private final AdminProductService adminProductService;

    public AdminProductController(AdminProductService adminProductService) {
        this.adminProductService = adminProductService;
    }

    // GET /api/admin/products  -> dùng trong loadProducts() bên admin.html
    @GetMapping
    public List<ProductDto> getAll() {
        return adminProductService.getAllProducts();
    }

    // GET /api/admin/products/{id}  (dùng sau này khi sửa sản phẩm)
    @GetMapping("/{id}")
    public ProductDto getById(@PathVariable Long id) {
        return adminProductService.getProductById(id);
    }

    // POST /api/admin/products  -> dùng trong addProduct() bên admin.html
    @PostMapping
    public ProductDto create(@RequestBody ProductDto dto) {
        return adminProductService.createProduct(dto);
    }

    // PUT /api/admin/products/{id}  (sau này dùng cho editProduct)
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody ProductDto dto) {
        adminProductService.updateProduct(id, dto);
    }

    // DELETE /api/admin/products/{id}  -> deleteProduct(id) bên admin.html
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        adminProductService.deleteProduct(id);
    }
}
