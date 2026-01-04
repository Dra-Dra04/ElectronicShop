package com.ecomelectronics.product_service.dto;

import java.math.BigDecimal;
import java.util.List; // Nhớ import List

public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String brand;
    private Integer stock;
    private String imageUrl; // Ảnh đại diện chính
    private Long categoryId;
    private String categoryName;

    // --- HỨNG LIST ẢNH TỪ FRONTEND ---
    private List<ProductImageDto> images;

    // --- GETTERS & SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public List<ProductImageDto> getImages() { return images; }
    public void setImages(List<ProductImageDto> images) { this.images = images; }
}