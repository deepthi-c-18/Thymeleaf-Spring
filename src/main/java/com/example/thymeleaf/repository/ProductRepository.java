package com.example.thymeleaf.repository;

import com.example.thymeleaf.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    List<Product> findByCategory(String category);

    List<Product> findByNameIgnoreCaseContaining(String name);

    @Query("SELECT p FROM Product p WHERE p.name ILIKE %:searchTerm% OR p.description ILIKE %:searchTerm%")
    List<Product> searchByNameOrDescription(@Param("searchTerm") String searchTerm);

    List<Product> findByStockQuantityLessThan(Integer quantity);
}
