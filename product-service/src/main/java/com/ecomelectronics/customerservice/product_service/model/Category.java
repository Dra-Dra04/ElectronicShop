package com.ecomelectronics.customerservice.product_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "Categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 500)
    private String description;

    public Category() {}

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
    @OneToMany(mappedBy = "category")
    private List<Product> products;

}
