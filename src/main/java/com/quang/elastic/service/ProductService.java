package com.quang.elastic.service;

import com.quang.elastic.dto.request.ProductCreateRequest;
import com.quang.elastic.dto.request.ProductUpdateRequest;
import com.quang.elastic.dto.response.ProductResponse;
import com.quang.elastic.entity.Product;
import com.quang.elastic.mapper.ProductMapper;
import com.quang.elastic.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;

    public ProductResponse createProduct(ProductCreateRequest request) {
        if (productRepository.existsProductByName(request.getName()))
            throw new IllegalArgumentException("Product already exists");

        Product product = productMapper.toProduct(request);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(String id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        productMapper.updateProduct(product,request);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    public List<ProductResponse> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        return productMapper.toProductResponse(product);
    }

    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public List<ProductResponse> searchProduct(String keyword, Double minPrice) {
        return productRepository.findProductsByKeywordAndMinPrice(keyword, minPrice)
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }
    public List<ProductResponse> searchProductByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }
    public List<ProductResponse> searchProductByPriceBetween(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

}