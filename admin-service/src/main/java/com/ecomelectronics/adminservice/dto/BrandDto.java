package com.ecomelectronics.adminservice.dto;

public class BrandDto {

    private Long id;
    private String name;
    private Long productCount; // có thể null khi tạo mới

    public BrandDto() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getProductCount() { return productCount; }
    public void setProductCount(Long productCount) { this.productCount = productCount; }
}
