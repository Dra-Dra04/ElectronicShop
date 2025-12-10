package com.ecomelectronics.adminservice.dto;

import java.util.ArrayList;
import java.util.List;

public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private Long brandId;
    private String brandName;
    private Long categoryId;
    private String categoryName;
    private Double price;
    private Integer stock;
    private String status;   // IN_STOCK / OUT_OF_STOCK / ...
    private String imageUrl; // ảnh chính (main)

    // NEW: danh sách ảnh phụ / nhiều ảnh
    private List<ProductImageDto> images = new ArrayList<>();

    public ProductDto() {
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<ProductImageDto> getImages() { return images; }
    public void setImages(List<ProductImageDto> images) { this.images = images; }
}
