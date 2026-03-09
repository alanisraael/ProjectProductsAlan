package com.project.alan.repositories;

import com.project.alan.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("ProductosRepository Tests")
public class ProductosRepositoryTest {

    @Autowired
    private ProductRepository productosRepository;

    private Product producto1;
    private Product producto2;
    private Product producto3;

    @BeforeEach
    public void setUp() {
        producto1 = new Product(
            "Laptop HP",
            "Laptop de 15 pulgadas",
            new BigDecimal("999.99"),
            50,
            "Electrónica",
            LocalDateTime.now(),
            true
        );

        producto2 = new Product(
            "Monitor LG",
            "Monitor 24 pulgadas",
            new BigDecimal("299.99"),
            30,
            "Electrónica",
            LocalDateTime.now(),
            true
        );

        producto3 = new Product(
            "Teclado Mecánico",
            "Teclado RGB",
            new BigDecimal("149.99"),
            100,
            "Accesorios",
            LocalDateTime.now(),
            false
        );

        productosRepository.saveAll(List.of(producto1, producto2, producto3));
    }

    @Test
    @DisplayName("Debe guardar un producto")
    public void testSaveProduct() {
        Product nuevoProducto = new Product(
            "Mouse Logitech",
            "Mouse inalámbrico",
            new BigDecimal("49.99"),
            200,
            "Accesorios",
            LocalDateTime.now(),
            true
        );

        Product guardado = productosRepository.save(nuevoProducto);
        assertNotNull(guardado.getId());
        assertEquals("Mouse Logitech", guardado.getNombre());
    }

    @Test
    @DisplayName("Debe obtener todos los productos")
    public void testFindAll() {
        List<Product> productos = productosRepository.findAll();
        assertFalse(productos.isEmpty());
        assertTrue(productos.size() >= 3);
    }

    @Test
    @DisplayName("Debe obtener un producto por ID")
    public void testFindById() {
        Long id = producto1.getId();
        Optional<Product> encontrado = productosRepository.findById(id);
        assertTrue(encontrado.isPresent());
        assertEquals("Laptop HP", encontrado.get().getNombre());
    }

    @Test
    @DisplayName("Debe retornar vacío si producto no existe")
    public void testFindByIdNotFound() {
        Optional<Product> encontrado = productosRepository.findById(9999L);
        assertFalse(encontrado.isPresent());
    }

    @Test
    @DisplayName("Debe encontrar productos activos")
    public void testFindByActivoTrue() {
        List<Product> productosActivos = productosRepository.findByActivoTrue();
        assertFalse(productosActivos.isEmpty());
        assertTrue(productosActivos.stream().allMatch(p -> p.getActivo() == true));
    }

    @Test
    @DisplayName("Debe encontrar productos por categoría")
    public void testFindByCategoria() {
        List<Product> electronica = productosRepository.findByCategoria("Electrónica");
        assertFalse(electronica.isEmpty());
        assertTrue(electronica.stream().allMatch(p -> p.getCategoria().equals("Electrónica")));
        assertEquals(2, electronica.size());
    }

    @Test
    @DisplayName("Debe retornar lista vacía si categoría no existe")
    public void testFindByCategoriaNotFound() {
        List<Product> productos = productosRepository.findByCategoria("Categoría Inexistente");
        assertTrue(productos.isEmpty());
    }

    @Test
    @DisplayName("Debe encontrar productos en rango de precio")
    public void testFindByPrecioBetween() {
        List<Product> productos = productosRepository.findByPrecioBetween(
            new BigDecimal("100.00"),
            new BigDecimal("500.00")
        );
        assertFalse(productos.isEmpty());
        assertTrue(productos.stream().allMatch(p ->
            p.getPrecio().compareTo(new BigDecimal("100.00")) >= 0 &&
            p.getPrecio().compareTo(new BigDecimal("500.00")) <= 0
        ));
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no hay productos en rango")
    public void testFindByPrecioBetweenEmpty() {
        List<Product> productos = productosRepository.findByPrecioBetween(
            new BigDecimal("5000.00"),
            new BigDecimal("10000.00")
        );
        assertTrue(productos.isEmpty());
    }

    @Test
    @DisplayName("Debe actualizar un producto")
    public void testUpdateProduct() {
        producto1.setNombre("Laptop Actualizada");
        producto1.setPrecio(new BigDecimal("1299.99"));

        Product actualizado = productosRepository.save(producto1);
        assertEquals("Laptop Actualizada", actualizado.getNombre());
        assertEquals(new BigDecimal("1299.99"), actualizado.getPrecio());
    }

    @Test
    @DisplayName("Debe eliminar un producto")
    public void testDeleteProduct() {
        Long id = producto1.getId();
        productosRepository.deleteById(id);

        Optional<Product> encontrado = productosRepository.findById(id);
        assertFalse(encontrado.isPresent());
    }

    @Test
    @DisplayName("Debe contar productos")
    public void testCount() {
        long cantidad = productosRepository.count();
        assertTrue(cantidad >= 3);
    }
}
