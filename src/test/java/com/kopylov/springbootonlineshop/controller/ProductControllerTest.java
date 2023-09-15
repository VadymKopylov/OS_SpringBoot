package com.kopylov.springbootonlineshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopylov.springbootonlineshop.dto.ProductDto;
import com.kopylov.springbootonlineshop.exceptions.ProductNotFoundException;
import com.kopylov.springbootonlineshop.model.Product;
import com.kopylov.springbootonlineshop.services.ProductService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void testProductController_CreateProduct_Return201IsCreated() throws Exception {
        given(productService.createProduct(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/products/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", CoreMatchers.is(productDto.getName())))
                .andExpect(jsonPath("$.price", CoreMatchers.is(productDto.getPrice())))
                .andExpect(jsonPath("$.creationDate", CoreMatchers.is(productDto.getCreationDate())));
    }

    @Test
    void testProductController_GetProducts_ReturnListOfProducts() throws Exception {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1L, "Phone", 1500, LocalDateTime.now()));

        given(productService.findAll(anyString())).willReturn(productList);

        ResultActions response = mockMvc.perform(get("/products")
                .param("sort", "name"));


        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Phone")))
                .andExpect(jsonPath("$[0].price", is(1500.0)))
                .andExpect(jsonPath("$[0].creationDate").exists());
    }

    @Test
    void testProductController_GetProduct_Return404NotFound() throws Exception {
        when(productService.getById(1)).thenThrow(ProductNotFoundException.class);

        mockMvc.perform(get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testProductController_GetProduct_ReturnProductDto() throws Exception {
        when(productService.getById(1)).thenReturn(productDto);

        ResultActions response = mockMvc.perform(get("/products/1/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(productDto.getName())))
                .andExpect(jsonPath("$.price", CoreMatchers.is(productDto.getPrice())))
                .andExpect(jsonPath("$.creationDate", CoreMatchers.is(productDto.getCreationDate())));
    }

    @Test
    void testProductController_UpdateProduct_ReturnProductDto() throws Exception {
        when(productService.update(1, productDto)).thenReturn(productDto);

        ResultActions response = mockMvc.perform(put("/products/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(productDto.getName())))
                .andExpect(jsonPath("$.price", CoreMatchers.is(productDto.getPrice())))
                .andExpect(jsonPath("$.creationDate", CoreMatchers.is(productDto.getCreationDate())));
    }

    @Test
    void testProductController_DeleteProduct_ReturnString() throws Exception {
        doNothing().when(productService).delete(1);

        ResultActions response = mockMvc.perform(delete("/products/1/delete")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}