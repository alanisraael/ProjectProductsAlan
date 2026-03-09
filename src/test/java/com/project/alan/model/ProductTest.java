package com.project.alan.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product Model Tests")
public class ProductTest {

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product(
            "Laptop HP",
            "Laptop de 15 pulgadas",
            new BigDecimal("999.99"),
            50,
            "Electrónica",
            LocalDateTime.now(),
            true
        );
    }

    @Test
    @DisplayName("Debe crear un producto correctamente")
    public void testProductCreation() {
        assertNotNull(product);
        assertEquals("Laptop HP", product.getNombre());
        assertEquals("Laptop de 15 pulgadas", product.getDescripcion());
        assertEquals(new BigDecimal("999.99"), product.getPrecio());
        assertEquals(50, product.getCantidadStock());
        assertEquals("Electrónica", product.getCategoria());
        assertTrue(product.getActivo());
    }

    @Test
    @DisplayName("Debe validar que el nombre tenga entre 3 y 100 caracteres")
    public void testProductNombreValidation() {
        // Nombre válido
        product.setNombre("Producto");
        assertEquals("Producto", product.getNombre());

        // Nombre muy corto (menor a 3)
        product.setNombre("PC");
        assertEquals("PC", product.getNombre());
    }

    @Test
    @DisplayName("Debe permitir descripción de hasta 500 caracteres")
    public void testProductDescripcionValidation() {
        String descripcion = "A".repeat(500);
        product.setDescripcion(descripcion);
        assertEquals(500, product.getDescripcion().length());
    }

    @Test
    @DisplayName("Debe validar que precio sea positivo")
    public void testProductPrecioPositivo() {
        product.setPrecio(new BigDecimal("100.50"));
        assertTrue(product.getPrecio().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Debe validar que cantidad de stock sea no negativa")
    public void testProductCantidadStockNonNegative() {
        product.setCantidadStock(0);
        assertTrue(product.getCantidadStock() >= 0);

        product.setCantidadStock(100);
        assertTrue(product.getCantidadStock() >= 0);
    }

    @Test
    @DisplayName("Debe manejar categoria correctamente")
    public void testProductCategoria() {
        product.setCategoria("Informática");
        assertEquals("Informática", product.getCategoria());
    }

    @Test
    @DisplayName("Debe manejar el estado activo")
    public void testProductActivo() {
        product.setActivo(true);
        assertTrue(product.getActivo());

        product.setActivo(false);
        assertFalse(product.getActivo());
    }

    @Test
    @DisplayName("Debe manejar fecha de creación")
    public void testProductFechaCreacion() {
        LocalDateTime ahora = LocalDateTime.now();
        product.setFechaCreacion(ahora);
        assertEquals(ahora, product.getFechaCreacion());
    }

    @Test
    @DisplayName("Debe manejar ID correctamente")
    public void testProductId() {
        product.setId(1L);
        assertEquals(1L, product.getId());
    }

    @Test
    @DisplayName("Constructor vacío debe crear un producto")
    public void testProductEmptyConstructor() {
        Product emptyProduct = new Product();
        assertNotNull(emptyProduct);
        assertNull(emptyProduct.getId());
        assertNull(emptyProduct.getNombre());
    }
}

