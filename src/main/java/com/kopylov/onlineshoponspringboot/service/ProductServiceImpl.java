package com.kopylov.onlineshoponspringboot.service;

import com.kopylov.onlineshoponspringboot.dao.ProductsDao;
import com.kopylov.onlineshoponspringboot.model.Product;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductsDao productsDao;
    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public ProductServiceImpl(ProductsDao productsDao) {
        this.productsDao = productsDao;
    }

    public void save(Product product) {
        product.setCreationDate(setFormattedTime(LocalDateTime.now()));
        productsDao.save(product);
    }

    public List<Product> index(String sortCriteria) {
        return productsDao.index()
                .stream()
                .sorted(createComparatorByCriteria(sortCriteria))
                .collect(Collectors.toList());
    }

    public Product show(int id) {
        return productsDao.show(id).orElseThrow(() ->
                new RuntimeException(String.format("Product with id: %d not found.", id)));
    }

    public List<Product> show(String name) {
        return productsDao.show(name);
    }

    public void update(Product product) {
        product.setCreationDate(setFormattedTime(LocalDateTime.now()));
        productsDao.update(product);
    }

    public void delete(int id) {
        productsDao.delete(id);
    }

    LocalDateTime setFormattedTime(LocalDateTime creationDate) {
        String formattedDateTime = creationDate.format(DATE_TIME_FORMATTER);
        return LocalDateTime.parse(formattedDateTime, DATE_TIME_FORMATTER);
    }

    private Comparator<Product> createComparatorByCriteria(String sortCriteria) {
        if (Objects.equals("name", sortCriteria)) {
            return Comparator.comparing(Product::getName);
        } else if (Objects.equals("price", sortCriteria)) {
            return Comparator.comparing(Product::getPrice);
        } else if (Objects.equals("date", sortCriteria)) {
            return Comparator.comparing(Product::getCreationDate);
        } else {
            return Comparator.comparing(Product::getName);
        }
    }
}
