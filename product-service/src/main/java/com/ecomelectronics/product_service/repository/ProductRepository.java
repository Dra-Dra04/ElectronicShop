package com.ecomelectronics.product_service.repository;
import org.springframework.data.jpa.repository.Query;

import com.ecomelectronics.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory_Id(Long categoryId);

    List<Product> findByBrandContainingIgnoreCase(String brand);

    List<Product> findByNameContainingIgnoreCase(String keyword);
    @Query("select distinct p.brand from Product p where p.brand is not null and p.brand <> ''")
    List<String> findDistinctBrands();

    @Query("""
    select p from Product p
    where (:categoryId is null or p.category.id = :categoryId)
      and (:brand is null or p.brand = :brand)
      and (
           :keyword is null or :keyword = '' 
           or lower(p.name) like lower(concat('%', :keyword, '%'))
           or lower(p.description) like lower(concat('%', :keyword, '%'))
      )
""")
    List<Product> search(
            @Param("categoryId") Long categoryId,
            @Param("brand") String brand,
            @Param("keyword") String keyword
    );
    @Query("""
       select p from Product p
       left join fetch p.images
       where p.id = :id
    """)
    Optional<Product> findByIdWithImages(@Param("id") Long id);
}

