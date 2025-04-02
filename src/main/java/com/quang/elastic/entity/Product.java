package com.quang.elastic.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Builder
@Document(indexName = "products")
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    private String id;
    @Field(type = FieldType.Text, name = "name")
    private String name;
    @Field(type = FieldType.Text, name = "description")
    private String description;
    @Field(type = FieldType.Double, name = "price")
    private Double price;

}
