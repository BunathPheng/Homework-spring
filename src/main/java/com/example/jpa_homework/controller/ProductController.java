package com.example.jpa_homework.controller;

import com.example.jpa_homework.model.Product;
import com.example.jpa_homework.model.dto.request.ProductRequest;
import com.example.jpa_homework.model.dto.response.Response;
import com.example.jpa_homework.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.example.jpa_homework.utils.RequestUtils.getPaginatedResponse;
import static com.example.jpa_homework.utils.RequestUtils.getResponse;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Create a new product", description = "Accepts a product request payload and creates a new product. Returns the created product.")
    public ResponseEntity<Response<Product>> createProduct(@Valid @RequestBody ProductRequest product) {
        Product createdProduct = productService.save(product);
        return ResponseEntity.created(URI.create("")).body(getResponse(CREATED,"Created new product successfully",createdProduct));
    }

    @GetMapping
    @Operation(summary = "Get all products (paginated)", description = "Returns a paginated list of all products. Accepts page and size as query parameters.")
    public ResponseEntity<Response<List<Product>>> getAllProducts(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        List<Product> products = productService.getAllProducts(page, size);
        int total = products.size();
        return ResponseEntity.ok(getPaginatedResponse(OK,"Get all products successfully",products,page,size,total));
    }

    @GetMapping("/{product-id}")
    @Operation(summary = "Get product by ID", description = "Fetches a product using its unique ID. Returns 404 if not found.")
    public ResponseEntity<Response<Product>> getProductById(@PathVariable("product-id") Long productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(getResponse(OK,"Get product id: "+productId+" successfully",product));
    }


    @PutMapping("/{product-id}")
    @Operation(summary = "Update product by ID", description = "Updates an existing product with the given ID using the request body. Returns the updated product.")
    public ResponseEntity<Response<Product>> updateProductById(@PathVariable("product-id") Long productId, @Valid @RequestBody ProductRequest productRequest) {
        Product product = productService.update(productId,productRequest);
        return ResponseEntity.ok(getResponse(OK,"Updated product id: "+productId+" successfully",product));
    }

    @DeleteMapping("/{product-id}")
    @Operation(summary = "Delete product by ID", description = "Deletes a product by its ID. Returns HTTP 200 if the product is successfully deleted.")
    public ResponseEntity<Response<?>> deleteProductById(@PathVariable("product-id") Long productId) {
        productService.remove(productId);
        return ResponseEntity.ok(getResponse(OK,"Deleted product id: "+productId+" successfully",null));
    }

    @GetMapping("/search")
    @Operation(summary = "Search products by name", description = "Returns a list of products that contain the given name (case-insensitive partial match).")
    public ResponseEntity<Response<List<Product>>> getProductByName(@RequestParam String name) {
        List<Product> product = productService.getProductByNameIgnoreCases(name);
        return ResponseEntity.ok(getResponse(OK,"Get product name: '"+name+"' successfully",product));
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock products", description = "Returns a list of products with quantity less than the specified threshold.")
    public ResponseEntity<Response<List<Product>>> getProductsLowStock(@RequestParam int quantity) {
        List<Product> product = productService.getProductsLowStock(quantity);
        return ResponseEntity.ok(getResponse(OK,"Get product quantity under "+quantity+" successfully",product));
    }
}
