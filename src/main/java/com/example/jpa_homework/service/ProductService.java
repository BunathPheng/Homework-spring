package com.example.jpa_homework.service;

import com.example.jpa_homework.model.Product;
import com.example.jpa_homework.model.dto.request.ProductRequest;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts(int page, int size);

    Product getProductById(Long productId);

    Product save(ProductRequest product);

    Product update(Long productId, ProductRequest productRequest);

    void remove(Long productId);

    List<Product> getProductByNameIgnoreCases(String name);

    List<Product> getProductsLowStock(int quantity);
}
