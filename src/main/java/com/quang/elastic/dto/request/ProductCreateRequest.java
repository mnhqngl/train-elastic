package com.quang.elastic.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {

    private String name;
    private String description;
    private Double price;

}
