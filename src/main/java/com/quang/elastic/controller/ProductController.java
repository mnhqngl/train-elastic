package com.quang.elastic.controller;

import com.quang.elastic.dto.request.ProductCreateRequest;
import com.quang.elastic.dto.request.ProductUpdateRequest;
import com.quang.elastic.dto.response.ProductResponse;
import com.quang.elastic.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/products")
public class ProductController {

    ProductService service;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductCreateRequest request) {
        ProductResponse response = service.createProduct(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable String id, @RequestBody ProductUpdateRequest request) {
        ProductResponse response = service.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts() {
        List<ProductResponse> responses = service.getProducts();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id) {
        ProductResponse response = service.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable String id) {
        service.deleteProduct(id);
        return ResponseEntity.ok("Delete success");
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProduct(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double minPrice
    ) {
        List<ProductResponse> responses = service.searchProduct(keyword, minPrice);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<ProductResponse>> searchProductByName(
            @RequestParam String name
    ) {
        List<ProductResponse> responses = service.searchProductByName(name);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search/price")
    public ResponseEntity<List<ProductResponse>> searchProductByPriceBetween(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice
    ) {
        List<ProductResponse> responses = service.searchProductByPriceBetween(minPrice, maxPrice);
        return ResponseEntity.ok(responses);
    }
}