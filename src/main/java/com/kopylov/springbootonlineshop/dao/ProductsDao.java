package com.kopylov.springbootonlineshop.dao;

import com.kopylov.springbootonlineshop.dto.ProductDto;
import com.kopylov.springbootonlineshop.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductsDao {

    long save(Product product);

    Optional<Product> findById(long id);

    List<Product> findByName(String name);

    List<Product> findAll();

    void update(Product product);

    void delete(long id);
}
