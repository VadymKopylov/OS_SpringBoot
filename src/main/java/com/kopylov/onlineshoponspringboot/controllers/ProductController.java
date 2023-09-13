package com.kopylov.onlineshoponspringboot.controllers;

import com.kopylov.onlineshoponspringboot.model.Product;
import com.kopylov.onlineshoponspringboot.service.ProductServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductServiceImpl productServiceImpl;

    public ProductController(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    @GetMapping("")
    public String getProductPage(Model model, @RequestParam(value = "sort", required = false) String sort) {
        model.addAttribute("Products", productServiceImpl.index(sort));
        return "index";
    }

    @GetMapping(params = "search")
    public String getSearchPage(@RequestParam(value = "search", required = false) String name, Model model) {
        model.addAttribute("Products", productServiceImpl.show(name));
        return "show";
    }

    @GetMapping("/add")
    public String getAddProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "add";
    }

    @PostMapping()
    public String add(@ModelAttribute("product") Product product) {
        productServiceImpl.save(product);
        return "redirect:/product";
    }

    @GetMapping("/{id}/edit")
    public String getEditPage(Model model, @PathVariable("id") int id) {
        model.addAttribute("product", productServiceImpl.show(id));
        return "edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("product") Product product, @PathVariable("id") int id) {
        productServiceImpl.update(product);
        return "redirect:/product";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        productServiceImpl.delete(id);
        return "redirect:/product";
    }
}
