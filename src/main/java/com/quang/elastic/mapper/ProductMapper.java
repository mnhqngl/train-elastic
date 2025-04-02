package com.quang.elastic.mapper;

import com.quang.elastic.dto.request.ProductCreateRequest;
import com.quang.elastic.dto.request.ProductUpdateRequest;
import com.quang.elastic.dto.response.ProductResponse;
import com.quang.elastic.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductCreateRequest request);

    ProductResponse toProductResponse(Product product);

    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request);

}
