package com.quang.elastic.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequest {

    private String description;
    private Double price;

}
