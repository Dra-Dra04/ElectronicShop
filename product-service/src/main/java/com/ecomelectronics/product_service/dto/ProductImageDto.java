package com.ecomelectronics.product_service.dto;

public class ProductImageDto {
    private Long id;
    private String imageUrl;
    private Boolean isMain;
    private Integer sortOrder;

    // Constructor rỗng (Bắt buộc để Spring map JSON)
    public ProductImageDto() {}

    // Constructor đầy đủ
    public ProductImageDto(Long id, String imageUrl, Boolean isMain, Integer sortOrder) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.isMain = isMain;
        this.sortOrder = sortOrder;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean getIsMain() { return isMain; }
    public void setIsMain(Boolean isMain) { this.isMain = isMain; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}