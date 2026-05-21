package com.example.thymeleaf.service;

import com.example.thymeleaf.entity.Product;
import com.example.thymeleaf.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get all products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Get product by ID
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Create a new product
     */
    public Product createProduct(Product product) {
        // Validate SKU uniqueness
        if (productRepository.findBySku(product.getSku()).isPresent()) {
            throw new IllegalArgumentException("SKU already exists: " + product.getSku());
        }
        return productRepository.save(product);
    }

    /**
     * Update an existing product
     */
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));

        // Check if SKU is being changed and if new SKU already exists
        if (!product.getSku().equals(productDetails.getSku()) &&
                productRepository.findBySku(productDetails.getSku()).isPresent()) {
            throw new IllegalArgumentException("SKU already exists: " + productDetails.getSku());
        }

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setCategory(productDetails.getCategory());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setSku(productDetails.getSku());

        return productRepository.save(product);
    }

    /**
     * Delete a product
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    /**
     * Search products by name
     */
    public List<Product> searchByName(String name) {
        return productRepository.findByNameIgnoreCaseContaining(name);
    }

    /**
     * Get products by category
     */
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /**
     * Get low stock products
     */
    public List<Product> getLowStockProducts(Integer threshold) {
        return productRepository.findByStockQuantityLessThan(threshold);
    }

    /**
     * Get unique categories
     */
    public List<String> getAllCategories() {
        return productRepository.findAll()
                .stream()
                .map(Product::getCategory)
                .distinct()
                .sorted()
                .toList();
    }

    /**
     * Check if product exists
     */
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    /**
     * Count total products
     */
    public long countProducts() {
        return productRepository.count();
    }
}
