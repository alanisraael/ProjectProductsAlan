package com.project.alan.controller;
import com.project.alan.dto.ProductoInput;
import com.project.alan.model.Product;
import com.project.alan.repositories.ProductRepository;
import com.project.alan.services.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {
    private final ProductRepository productosRepository;
    private final ProductService productService;

    public ProductController(ProductRepository productosRepository, ProductService productService) {
        this.productosRepository = productosRepository;
        this.productService = productService;
    }

    // Endpoint para obtener todos los productos
    @GetMapping(path = "/productos")
    public List<Product> getAllProduct() {
        List<Product> productos = productosRepository.findAll();
        if (productos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No products available");
        }
        return productos;
    }

    // Endpoint para obtener un producto por su ID
    @GetMapping(path = "/productos/{id:[0-9]+}")
    public Product productById(@PathVariable Long id) {
        Optional<Product> productosOptional = productosRepository.findById(id);
        if (productosOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + " not available");
        }

        return productosOptional.get();
    }

    // Endpoint para obtener solo los productos activos
    @GetMapping(path = "/productos/activos")
    public List<Product> getActiveProducts() {
        Exception e = new Exception("No active products found");
        List<Product> activeProducts = productosRepository.findByActivoTrue();
        if (activeProducts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        }
        return activeProducts;
    }

    // Endpoint para obtener productos por categoría
    @GetMapping(path = "/productos/categoria")
    public List<Product> getProductsByCategoria(@RequestParam String categoria) {
        List<Product> productsByCategoria = productosRepository.findByCategoria(categoria);
        if (productsByCategoria.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No products found for category: " + categoria);
        }
        return productsByCategoria;
    }

    // Endpoint para obtener productos por rango de precio
    @GetMapping(path = "/productos/precio")
    public List<Product> getProductsByPrecioRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        List<Product> productsByPrecioRange = productosRepository.findByPrecioBetween(min, max);
        if (productsByPrecioRange.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No products found in price range: " + min + " - " + max);
        }
        return productsByPrecioRange;
    }

    // Endpoint para crear un nuevo producto
    @PostMapping(path = "/productos")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "BasicAuth")
    public String createProduct(@Valid @RequestBody ProductoInput newProductoInput) throws Exception {
        try {
            return productService.createProduct(newProductoInput);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Endpoint para eliminar un producto (marcarlo como inactivo)
    @DeleteMapping(path = "/productos/{id:[0-9]+}")
    @SecurityRequirement(name = "BasicAuth")
    public String deleteProduct(@PathVariable Long id) {
        try {
            return productService.deleteProduct(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Endpoint para actualizar un producto existente
    @PutMapping(path = "/productos/{id:[0-9]+}")
    @SecurityRequirement(name = "BasicAuth")
    public String updateProduct(@PathVariable Long id, @Valid @RequestBody ProductoInput productoInput) {
        try {
            return productService.updateProduct(id, productoInput);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
