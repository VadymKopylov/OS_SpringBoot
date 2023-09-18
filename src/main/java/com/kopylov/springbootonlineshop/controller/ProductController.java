package com.kopylov.springbootonlineshop.controller;

import com.kopylov.springbootonlineshop.dto.ProductDto;
import com.kopylov.springbootonlineshop.exceptions.ProductNotFoundException;
import com.kopylov.springbootonlineshop.model.Product;
import com.kopylov.springbootonlineshop.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<List<Product>> getProducts(@RequestParam(value = "sort", required = false) String sort) {
        List<Product> allProducts = productService.findAll(sort);
        if (allProducts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productService.findAll(sort));
    }

    @GetMapping(params = "search")
    public String getSearchPage(@RequestParam(value = "search", required = false) String name, Model model) {
        model.addAttribute("Products", productService.getByName(name));
        return "search";
    }

    @GetMapping("/")
    public String getAddProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "add";
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDto> add(@RequestBody @Valid ProductDto productDto) {
        ProductDto createdProduct = productService.createProduct(productDto);
        URI uri = URI.create("/create/" + createdProduct.getId());
        return ResponseEntity.created(uri).body(createdProduct);
        //return new ResponseEntity<>(productService.createProduct(productDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(productService.getById(id));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ProductDto> update(@PathVariable long id, @RequestBody @Valid ProductDto productDto) {
        try{
            ProductDto updatedProduct = productService.update(id, productDto);
            return ResponseEntity.ok(updatedProduct);
        }catch (ProductNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        productService.delete(id);
        return new ResponseEntity<>("Product delete", HttpStatus.OK);
    }
}
