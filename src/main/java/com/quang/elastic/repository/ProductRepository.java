package com.quang.elastic.repository;

import com.quang.elastic.entity.Product;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {

    boolean existsProductByName(String name);

    // Custom methods for searching products using Elasticsearch
    // Find products by name (containing a keyword)
    List<Product> findByNameContaining(String name);

    // Find products by price range
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    // Find products by name (ignoring case)
    List<Product> findByNameContainingIgnoreCase(String name);

    // 7. Custom query using @Query annotation (Elasticsearch Query DSL)
    @Query("""
            {
              "bool": {
                "must": [
                  {
                    "match": {
                      "name": "?0"
                    }
                  },
                  {
                    "range": {
                      "price": {
                        "gt": ?1
                      }
                    }
                  }
                ]
              }
            }
            """)
    List<Product> findProductsByKeywordAndMinPrice(String keyword, Double minPrice);

    // Find all product
    List<Product> findAll();
}