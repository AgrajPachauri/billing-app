package com.app.billing.controller;

import com.app.billing.model.Product;
import com.app.billing.model.ProductPrice;
import com.app.billing.repository.ProductRepository;
import com.app.billing.repository.ProductPriceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ProductPriceRepository priceRepo;

    // Add product with price
    @PostMapping
    public String addProduct(@RequestBody ProductPrice request) {

        Product product = request.getProduct();

        product = productRepo.save(product);

        request.setProduct(product);
        priceRepo.save(request);

        return "Product added";
    }

    // Get all products
    @GetMapping
    public List<Product> getProducts() {
        return productRepo.findAll();
    }

    // Get products by area (with price)
    @GetMapping("/by-area")
    public List<ProductPrice> getByArea(@RequestParam String area) {
        return priceRepo.findByArea(area);
    }

    // Test
    @GetMapping("/test")
    public String test() {
        return "Product API working";
    }
}