package com.quang.elastic.service;

import com.quang.elastic.dto.request.ProductCreateRequest;
import com.quang.elastic.dto.request.ProductUpdateRequest;
import com.quang.elastic.dto.response.ProductResponse;
import com.quang.elastic.entity.ProductMySQL;
import com.quang.elastic.mapper.ProductMySQLMapper;
import com.quang.elastic.repository.ProductMySQLRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductMySQLService {
    ProductMySQLRepository productRepository;
    ProductMySQLMapper productMapper;

    public ProductResponse createProduct(ProductCreateRequest request) {
        if (productRepository.existsByName(request.getName()))
            throw new IllegalArgumentException("Product already exists");

        ProductMySQL product = productMapper.toProduct(request);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        ProductMySQL product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        productMapper.updateProduct(request, product);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    public List<ProductResponse> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public ProductResponse getProductById(Long id) {
        ProductMySQL product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        return productMapper.toProductResponse(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found");
        }
        productRepository.deleteById(id);
    }
}
