package com.kopylov.onlineshoponspringboot.model;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private int id;
    private String name;
    private double price;
    private LocalDateTime creationDate;

}
