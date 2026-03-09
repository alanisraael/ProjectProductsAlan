package com.project.alan.repositories;

import com.project.alan.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActivoTrue();
    List<Product> findByCategoria(String categoria);
    List<Product> findByPrecioBetween(BigDecimal min, BigDecimal max);
}
