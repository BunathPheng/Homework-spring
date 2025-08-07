package com.example.jpa_homework.service.impl;

import com.example.jpa_homework.exception.BadRequestException;
import com.example.jpa_homework.exception.ResourceNotFoundException;
import com.example.jpa_homework.model.Product;
import com.example.jpa_homework.model.dto.request.ProductRequest;
import com.example.jpa_homework.repository.ProductRepository;
import com.example.jpa_homework.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public Product save(ProductRequest product) {
        Product newProduct = productRepository.save(product);
        if(newProduct == null) {
            throw new BadRequestException("Product not saved");
        }
        return newProduct;
    }

    @Override
    public Product update(Long productId, ProductRequest productRequest) {
        Product updatedProduct = productRepository.update(productId,productRequest);
        if(updatedProduct == null) {
            throw new BadRequestException("Product not updated");
        }
        return updatedProduct;
    }

    @Override
    public void remove(Long productId) {
        productRepository.remove(productId);
    }

    @Override
    public List<Product> getProductByNameIgnoreCases(String name) {
        return productRepository.findProductsByNameIgnoreCases(name);
    }

    @Override
    public List<Product> getProductsLowStock(int quantity) {
        return productRepository.findProductsLowStock(quantity);
    }

    @Override
    public List<Product> getAllProducts(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page must be non-negative and size must be positive");
        }
        List<Product> products = productRepository.findAllPaginated(page,size);
        if(products.isEmpty()){
           return new ArrayList<>();
        }
        return products;
    }

    public Product getProductById(Long id){
        Product product = productRepository.findById(id);
        if(product == null){
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        }
        return product;
    }
}
