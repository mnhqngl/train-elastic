package com.quang.elastic.repository;

import com.quang.elastic.entity.ProductMySQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMySQLRepository extends JpaRepository<ProductMySQL, Long> {
    boolean existsByName(String name);
}
