package com.example.jpa_homework.repository;

import com.example.jpa_homework.exception.ResourceNotFoundException;
import com.example.jpa_homework.model.Product;
import com.example.jpa_homework.model.dto.request.ProductRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ProductRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Product> findAllPaginated(int page, int size) {
        TypedQuery<Product> products = em.createQuery("select p from Product p", Product.class);
        products.setFirstResult((page - 1) * size);
        products.setMaxResults(size);
        return products.getResultList();
    }

    public Product findById(Long id) {
        return em.find(Product.class, id);
    }

    public Product save(ProductRequest productsRequest) {
        Product product = Product.builder()
                .name(productsRequest.getName())
                .price(productsRequest.getPrice())
                .quantity(productsRequest.getQuantity())
                .build();
        em.persist(product);
        return product;
    }

    public Product update(Long productId, ProductRequest productRequest) {
        Product findProduct = em.find(Product.class, productId);
        if (findProduct == null || productId == null) {
            throw new ResourceNotFoundException("Product not found");
        }

        em.detach(findProduct);

        findProduct.setName(productRequest.getName());
        findProduct.setPrice(productRequest.getPrice());
        findProduct.setQuantity(productRequest.getQuantity());

        return em.merge(findProduct);

    }

    public void remove(Long productId) {
        Product product = em.find(Product.class, productId);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found");
        }
        em.remove(product);
    }

    public List<Product> findProductsByNameIgnoreCases(String searchName) {
        return em.createQuery("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(:searchTerm)", Product.class)
                .setParameter("searchTerm", "%" + searchName + "%")
                .getResultList();
    }

    public List<Product> findProductsLowStock(int quantity) {
        return em.createQuery("SELECT p FROM Product p WHERE p.quantity < :qty",Product.class)
                .setParameter("qty", quantity )
                .getResultList();
    }
}

