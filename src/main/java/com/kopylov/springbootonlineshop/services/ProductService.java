package com.kopylov.springbootonlineshop.services;

import com.kopylov.springbootonlineshop.dto.ProductDto;
import com.kopylov.springbootonlineshop.model.Product;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);

    List<Product> findAll(String sortCriteria);

    ProductDto getById(long id);

    List<Product> getByName(String name);

    ProductDto update(long id, ProductDto productDto);

    void delete(long id);
}
