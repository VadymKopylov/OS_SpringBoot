package com.kopylov.springbootonlineshop.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
public class ProductDto {

    private long id;
    private String name;
    private double price;
    private LocalDateTime creationDate;
}

