package com.ecomelectronics.product_service.repository;
import org.springframework.data.jpa.repository.Query;

import com.ecomelectronics.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory_Id(Long categoryId);

    List<Product> findByBrandContainingIgnoreCase(String brand);

    List<Product> findByNameContainingIgnoreCase(String keyword);
    @Query("select distinct p.brand from Product p where p.brand is not null and p.brand <> ''")
    List<String> findDistinctBrands();
}

