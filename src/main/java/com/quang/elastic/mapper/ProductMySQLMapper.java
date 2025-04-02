package com.quang.elastic.mapper;

import com.quang.elastic.dto.request.ProductCreateRequest;
import com.quang.elastic.dto.request.ProductUpdateRequest;
import com.quang.elastic.dto.response.ProductResponse;
import com.quang.elastic.entity.ProductMySQL;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMySQLMapper {
    ProductMySQL toProduct(ProductCreateRequest request);

    ProductResponse toProductResponse(ProductMySQL product);

    void updateProduct(ProductUpdateRequest request, @MappingTarget ProductMySQL product);
}
