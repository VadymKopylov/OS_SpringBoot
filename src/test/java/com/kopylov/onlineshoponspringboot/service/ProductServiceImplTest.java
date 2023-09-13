package com.kopylov.onlineshoponspringboot.service;

import com.kopylov.onlineshoponspringboot.dao.ProductsDao;
import com.kopylov.onlineshoponspringboot.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceImplTest {

    @MockBean
    private ProductsDao productsDao;
    @Autowired
    private ProductServiceImpl productServiceImpl;

    @BeforeEach
    public void setUp() {
        Product firstProduct = new Product(1, "Phone", 1000, productServiceImpl.setFormattedTime(LocalDateTime.now()));
        Product secondProduct = new Product(2, "Car", 15000, productServiceImpl.setFormattedTime(LocalDateTime.now()));
        Product thirdProduct = new Product(3, "Playstation", 800, productServiceImpl.setFormattedTime(LocalDateTime.now()));
        List<Product> products = new ArrayList<>();
        products.add(firstProduct);
        products.add(secondProduct);
        products.add(thirdProduct);

        when(productsDao.index()).thenReturn(products);
        when(productsDao.show("Phone")).thenReturn(List.of(firstProduct));
        when(productsDao.show(1)).thenReturn(Optional.of(firstProduct));
    }

    @Test
    public void testIndex() {
        List<Product> result = productServiceImpl.index("price");

        assertNotNull(result);

        assertEquals("Playstation", result.get(0).getName());
        assertEquals("Phone", result.get(1).getName());
        assertEquals("Car", result.get(2).getName());
    }

    @Test
    public void testShowById() {
        Product result = productServiceImpl.show(1);

        assertEquals("Phone", result.getName());
    }

    @Test
    public void testShowByName() {
        String name = "Phone";

        List<Product> result = productServiceImpl.show(name);

        assertEquals(name, result.get(0).getName());
    }

    @Test
    public void testSave() {
        Product product = new Product();
        productServiceImpl.save(product);
        verify(productsDao, times(1)).save(product);
    }

    @Test
    public void testUpdate() {
        Product product = new Product();
        productServiceImpl.update(product);
        verify(productsDao, times(1)).update(product);
    }

    @Test
    public void testDelete() {
        int id = 1;
        productServiceImpl.delete(id);
        verify(productsDao, times(1)).delete(id);
    }
}