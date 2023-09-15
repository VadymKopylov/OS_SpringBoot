package com.kopylov.springbootonlineshop.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    private long id;

    @NotEmpty(message = "Product name should not be empty")
    @Size(min = 2, max = 30, message = "Product name should be between 2 and 30 characters")
    private String name;

    @Min(value = 0, message = "Price should be greater than 0")
    private double price;

    @NotNull(message = "Creation date should not be null")
    private LocalDateTime creationDate;

}
