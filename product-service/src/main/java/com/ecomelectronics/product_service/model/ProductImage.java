package com.ecomelectronics.product_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ProductImages")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String imageUrl;

    // Ảnh này có phải ảnh chính không (để show to ra giữa)
    @Column(nullable = false)
    private Boolean isMain = false;

    // Thứ tự hiển thị trong list thumbnail
    @Column(nullable = false)
    private Integer sortOrder = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public ProductImage() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean getIsMain() { return isMain; }
    public void setIsMain(Boolean main) { isMain = main; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}
