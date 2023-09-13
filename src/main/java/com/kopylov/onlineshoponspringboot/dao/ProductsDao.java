package com.kopylov.onlineshoponspringboot.dao;

import com.kopylov.onlineshoponspringboot.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductsDao {

    void save(Product product);

    Optional<Product> show(int id);

    List<Product> show(String name);

    List<Product> index();

    void update(Product product);

    void delete(int id);
}
