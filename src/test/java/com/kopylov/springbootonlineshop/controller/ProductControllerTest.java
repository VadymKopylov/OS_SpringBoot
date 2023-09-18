package com.kopylov.springbootonlineshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopylov.springbootonlineshop.dto.ProductDto;
import com.kopylov.springbootonlineshop.exceptions.ProductNotFoundException;
import com.kopylov.springbootonlineshop.model.Product;
import com.kopylov.springbootonlineshop.services.ProductService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;

    private ProductDto productDto;

    @BeforeEach
    public void init() {
        productDto = ProductDto.builder()
                .id(1L)
                .name("Phone")
                .price(1000)
                .creationDate(LocalDateTime.now().withNano(0))
                .build();
    }

    @Test
    void testProductController_CreateProduct_Return400BadRequest() throws Exception {
        ProductDto productDtoWithEmptyValue = ProductDto.builder()
                .id(0)
                .name("")
                .price(0)
                .creationDate(null)
                .build();

        mockMvc.perform(post("/products/create"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testProductController_CreateProduct_Return201IsCreated() throws Exception {
        when(productService.createProduct(ArgumentMatchers.any())).thenReturn(productDto);

        mockMvc.perform(post("/products/create"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(header().string("Location", is("/create/1")))
                .andExpect(jsonPath("$.name", CoreMatchers.is(productDto.getName())))
                .andExpect(jsonPath("$.price", CoreMatchers.is(productDto.getPrice())))
                .andExpect(jsonPath("$.creationDate", CoreMatchers.is(productDto.getCreationDate().toString())))
                .andDo(print());
    }

    @Test
    void testProductController_GetProduct_Return404NotFound() throws Exception {
        when(productService.getById(1L)).thenThrow(ProductNotFoundException.class);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testProductController_GetProduct_ReturnProductDto() throws Exception {
        when(productService.getById(1)).thenReturn(productDto);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name", CoreMatchers.is(productDto.getName())))
                .andExpect(jsonPath("$.price", CoreMatchers.is(productDto.getPrice())))
                .andExpect(jsonPath("$.creationDate", CoreMatchers.is(productDto.getCreationDate().toString())));
    }

    @Test
    void testProductController_GetProducts_Return204NoContent() throws Exception {
        when(productService.findAll(anyString())).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/products"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void testProductController_GetProducts_ReturnListOfProductsAnd200Ok() throws Exception {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1L, "Phone", 1500, LocalDateTime.now()));
        productList.add(new Product(2L, "Car", 10000, LocalDateTime.now()));

        when(productService.findAll(anyString())).thenReturn(productList);

        mockMvc.perform(get("/products")
                        .param("sort", "name"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Phone")))
                .andExpect(jsonPath("$[0].price", is(1500.0)))
                .andExpect(jsonPath("$[0].creationDate").exists())
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Car")))
                .andExpect(jsonPath("$[1].price", is(10000.0)))
                .andExpect(jsonPath("$[1].creationDate").exists())
                .andDo(print());
    }

    @Test
    void testProductController_UpdateProduct_Return404NotFound() throws Exception {
        ProductDto notExistProductDto = ProductDto.builder()
                .name("Phone")
                .price(1000)
                .creationDate(LocalDateTime.now().withNano(0))
                .build();

        when(productService.update(2,notExistProductDto)).thenThrow(ProductNotFoundException.class);

        mockMvc.perform(put("/products/2/update").contentType("application/json")
                .content(objectMapper.writeValueAsString(notExistProductDto)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testProductController_UpdateProduct_Return400BadRequest() throws Exception {
        ProductDto notExistProductDto = ProductDto.builder()
                .name("")
                .price(0)
                .creationDate(null)
                .build();

        mockMvc.perform(put("/products/123/update").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notExistProductDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testProductController_UpdateProduct_ReturnProductDtoAnd200Ok() throws Exception {
        given(productService.update(ArgumentMatchers.anyLong(),ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));
        //when(productService.update(1,productDto)).thenReturn(productDto);

        mockMvc.perform(put("/products/1/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name", CoreMatchers.is(productDto.getName())))
                .andExpect(jsonPath("$.price", CoreMatchers.is(productDto.getPrice())))
                .andExpect(jsonPath("$.creationDate", CoreMatchers.is(productDto.getCreationDate().toString())))
                .andDo(print());
    }

    @Test
    void testProductController_DeleteProduct_ReturnString() throws Exception {
        doNothing().when(productService).delete(1);

        mockMvc.perform(delete("/products/1/delete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}