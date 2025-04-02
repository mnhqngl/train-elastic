package com.quang.elastic.repository;

import com.quang.elastic.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    boolean existsProductByName(String name);

    // Custom methods for searching products
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% AND p.price > :minPrice")
    List<Product> findProductsByKeywordAndMinPrice
    (@Param("keyword") String keyword, @Param("minPrice") double minPrice);

}
