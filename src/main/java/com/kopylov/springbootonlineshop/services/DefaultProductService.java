package com.kopylov.springbootonlineshop.services;

import com.kopylov.springbootonlineshop.dao.ProductsDao;
import com.kopylov.springbootonlineshop.dto.ProductDto;
import com.kopylov.springbootonlineshop.exceptions.ProductNotFoundException;
import com.kopylov.springbootonlineshop.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DefaultProductService implements ProductService {

    private final ProductsDao productsDao;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .creationDate(LocalDateTime.now())
                .build();

        long productId = productsDao.save(product);
        productDto.setId(productId);
        return productDto;
    }

    @Override
    public List<Product> findAll(String sortCriteria) {
        return productsDao.findAll()
                .stream()
                .sorted(createComparatorByCriteria(sortCriteria))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getById(long id) {
        Product product = productsDao.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Product with id: " + id + " not found."));
        return mapToDto(id, product);
    }

    @Override
    public List<Product> getByName(String name) {
        return productsDao.findByName(name);
    }

    @Override
    public ProductDto update(long id, ProductDto productDto) {
        Product product = productsDao.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Product with id: " + id + " not found."));

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());

        productsDao.update(product);

        return mapToDto(id, product);
    }

    @Override
    public void delete(long id) {
        productsDao.delete(id);
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

    private ProductDto mapToDto(long id, Product product) {
        return ProductDto.builder()
                .id(id)
                .name(product.getName())
                .price(product.getPrice())
                .creationDate(product.getCreationDate())
                .build();
    }

    private Product mapToEntity(ProductDto productDto) {
        return Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .creationDate(productDto.getCreationDate())
                .build();
    }
}
