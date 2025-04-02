package com.quang.elastic.controller;

import com.quang.elastic.dto.request.ProductCreateRequest;
import com.quang.elastic.dto.request.ProductUpdateRequest;
import com.quang.elastic.dto.response.ProductResponse;
import com.quang.elastic.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/products")
public class ProductController {

    ProductService service;

    @PostMapping
    public ProductResponse createProduct(@RequestBody ProductCreateRequest request) {
        return service.createProduct(request);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable String id, @RequestBody ProductUpdateRequest request) {
        return service.updateProduct(id, request);
    }

    @GetMapping
    public List<ProductResponse> getProducts() {
        return service.getProducts();
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable String id) {
        return service.getProductById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteProductById(@PathVariable String id) {
        service.deleteProduct(id);
        return "Delete success";
    }
}