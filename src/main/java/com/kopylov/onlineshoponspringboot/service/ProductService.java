package com.kopylov.onlineshoponspringboot.service;

import com.kopylov.onlineshoponspringboot.model.Product;

import java.util.List;

public interface ProductService {

    void save(Product product);

    List<Product> index(String sortCriteria);

    Product show(int id);

    List<Product> show(String name);

    void update(Product product);

    void delete(int id);
}
