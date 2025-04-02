package com.quang.elastic.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private String id;
    private String name;
    private String description;
    private Double price;

}
