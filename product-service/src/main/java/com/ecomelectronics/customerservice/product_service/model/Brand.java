package com.ecomelectronics.customerservice.product_service.model;

import jakarta.persistence.*;
import org.hibernate.Length;

@Entity
@Table(name = "Brands")
public class Brand{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 500)
    private String description;

    public  Brand(){}

    public Brand(String name, String description){
        this.name = name;
        this.description = description;
    }

    public Long getId(){return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

}