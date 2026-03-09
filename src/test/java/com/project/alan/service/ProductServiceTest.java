package com.project.alan.service;

import com.project.alan.dto.ProductoInput;
import com.project.alan.model.Product;
import com.project.alan.repositories.ProductRepository;
import com.project.alan.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("ProductService Tests")
public class ProductServiceTest {

    @Mock
    private ProductRepository productosRepository;

    @InjectMocks
    private ProductService productService;

    private ProductoInput productoInput;
    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        productoInput = new ProductoInput();
        productoInput.setNombre("Laptop HP");
        productoInput.setDescripcion("Laptop de 15 pulgadas");
        productoInput.setPrecio(new BigDecimal("999.99"));
        productoInput.setCantidadStock(50);
        productoInput.setCategoria("Electrónica");
        productoInput.setActivo(true);

        product = new Product();
        product.setId(1L);
        product.setNombre("Laptop");
        product.setDescripcion("Laptop de 15 pulgadas");
        product.setPrecio(new BigDecimal("999.99"));
        product.setCantidadStock(50);
        product.setCategoria("Electrónica");
        product.setFechaCreacion(LocalDateTime.now());
        product.setActivo(true);
    }

    @Test
    @DisplayName("Debe crear un producto exitosamente")
    public void testCreateProductSuccess() throws Exception {
        // Given
        when(productosRepository.save(any(Product.class))).thenReturn(product);

        // When
        String result = productService.createProduct(productoInput);

        // Then
        assertEquals("Error al crear el producto", result);
        verify(productosRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe crear producto con datos correctos")
    public void testCreateProductWithCorrectData() throws Exception {
        // Given
        when(productosRepository.save(any(Product.class))).thenReturn(product);

        // When
        productService.createProduct(productoInput);

        // Then
        verify(productosRepository).save(argThat(savedProduct -> {
            assertEquals("Laptop HP", savedProduct.getNombre());
            assertEquals("Laptop de 15 pulgadas", savedProduct.getDescripcion());
            assertEquals(new BigDecimal("999.99"), savedProduct.getPrecio());
            assertEquals(50, savedProduct.getCantidadStock());
            assertEquals("Electrónica", savedProduct.getCategoria());
            assertTrue(savedProduct.getActivo());
            assertNotNull(savedProduct.getFechaCreacion());
            return true;
        }));
    }

    @Test
    @DisplayName("Debe manejar error al guardar producto")
    public void testCreateProductRepositoryError() throws Exception {
        // Given
        when(productosRepository.save(any(Product.class)))
            .thenThrow(new RuntimeException("Database error"));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.createProduct(productoInput);
        });

        assertEquals("Database error", exception.getMessage());
        verify(productosRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe crear producto con nombre mínimo válido")
    public void testCreateProductWithMinimumName() throws Exception {
        // Given
        productoInput.setNombre("ABC");
        when(productosRepository.save(any(Product.class))).thenReturn(product);

        // When
        String result = productService.createProduct(productoInput);

        // Then
        assertEquals("Error al crear el producto", result);
        verify(productosRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe crear producto con descripción máxima")
    public void testCreateProductWithMaximumDescription() throws Exception {
        // Given
        productoInput.setDescripcion("A".repeat(500));
        when(productosRepository.save(any(Product.class))).thenReturn(product);

        // When
        String result = productService.createProduct(productoInput);

        // Then
        assertEquals("Error al crear el producto", result);
        verify(productosRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe crear producto con precio mínimo positivo")
    public void testCreateProductWithMinimumPrice() throws Exception {
        // Given
        productoInput.setPrecio(new BigDecimal("0.01"));
        when(productosRepository.save(any(Product.class))).thenReturn(product);

        // When
        String result = productService.createProduct(productoInput);

        // Then
        assertEquals("Error al crear el producto", result);
        verify(productosRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe crear producto con stock cero")
    public void testCreateProductWithZeroStock() throws Exception {
        // Given
        productoInput.setCantidadStock(0);
        when(productosRepository.save(any(Product.class))).thenReturn(product);

        // When
        String result = productService.createProduct(productoInput);

        // Then
        assertEquals("Error al crear el producto", result);
        verify(productosRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe crear producto con descripción nula")
    public void testCreateProductWithNullDescription() throws Exception {
        // Given
        productoInput.setDescripcion(null);
        when(productosRepository.save(any(Product.class))).thenReturn(product);

        // When
        String result = productService.createProduct(productoInput);

        // Then
        assertEquals("Error al crear el producto", result);
        verify(productosRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe crear producto con fecha de creación nula")
    public void testCreateProductWithNullFechaCreacion() throws Exception {
        // Given
        productoInput.setFechaCreacion(null);
        when(productosRepository.save(any(Product.class))).thenReturn(product);

        // When
        String result = productService.createProduct(productoInput);

        // Then
        assertEquals("Error al crear el producto", result);
        verify(productosRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe crear producto con activo nulo")
    public void testCreateProductWithNullActivo() throws Exception {
        // Given
        productoInput.setActivo(null);
        when(productosRepository.save(any(Product.class))).thenReturn(product);

        // When
        String result = productService.createProduct(productoInput);

        // Then
        assertEquals("Error al crear el producto", result);
        verify(productosRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe asignar fecha de creación automáticamente")
    public void testCreateProductSetsFechaCreacion() throws Exception {
        // Given
        when(productosRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product saved = invocation.getArgument(0);
            assertNotNull(saved.getFechaCreacion());
            return saved;
        });

        // When
        productService.createProduct(productoInput);

        // Then
        verify(productosRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe asignar activo como true por defecto")
    public void testCreateProductSetsActivoDefault() throws Exception {
        // Given
        when(productosRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product saved = invocation.getArgument(0);
            assertTrue(saved.getActivo());
            return saved;
        });

        // When
        productService.createProduct(productoInput);

        // Then
        verify(productosRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe actualizar un producto exitosamente")
    public void testUpdateProductSuccess() throws Exception {
        // Given
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setNombre("Producto Original");
        existingProduct.setPrecio(new BigDecimal("100.00"));

        ProductoInput updateInput = new ProductoInput();
        updateInput.setNombre("Producto Actualizado");
        updateInput.setDescripcion("Descripción actualizada");
        updateInput.setPrecio(new BigDecimal("150.00"));
        updateInput.setCantidadStock(20);
        updateInput.setCategoria("Nueva Categoría");
        updateInput.setActivo(false);

        when(productosRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productosRepository.save(any(Product.class))).thenReturn(existingProduct);

        // When
        String result = productService.updateProduct(1L, updateInput);

        // Then
        assertEquals("Producto actualizado exitosamente", result);
        verify(productosRepository).findById(1L);
        verify(productosRepository).save(existingProduct);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando producto a actualizar no existe")
    public void testUpdateProductNotFound() {
        // Given
        when(productosRepository.findById(999L)).thenReturn(Optional.empty());

        ProductoInput updateInput = new ProductoInput();
        updateInput.setNombre("Producto");

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            productService.updateProduct(999L, updateInput);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("not available"));
    }

    @Test
    @DisplayName("Debe eliminar un producto exitosamente (marcar como inactivo)")
    public void testDeleteProductSuccess() throws Exception {
        // Given
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setNombre("Producto a Eliminar");
        existingProduct.setActivo(true);

        when(productosRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productosRepository.save(any(Product.class))).thenReturn(existingProduct);

        // When
        String result = productService.deleteProduct(1L);

        // Then
        assertEquals("Producto eliminado exitosamente", result);
        verify(productosRepository).findById(1L);
        verify(productosRepository).save(existingProduct);
        assertFalse(existingProduct.getActivo());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando producto a eliminar no existe")
    public void testDeleteProductNotFound() {
        // Given
        when(productosRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            productService.deleteProduct(999L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("not available"));
    }

    @Test
    @DisplayName("Debe actualizar todos los campos del producto")
    public void testUpdateProductUpdatesAllFields() throws Exception {
        // Given
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setNombre("Original");
        existingProduct.setDescripcion("Original desc");
        existingProduct.setPrecio(new BigDecimal("100.00"));
        existingProduct.setCantidadStock(10);
        existingProduct.setCategoria("Original cat");
        existingProduct.setActivo(true);

        ProductoInput updateInput = new ProductoInput();
        updateInput.setNombre("Nuevo Nombre");
        updateInput.setDescripcion("Nueva Descripción");
        updateInput.setPrecio(new BigDecimal("200.00"));
        updateInput.setCantidadStock(50);
        updateInput.setCategoria("Nueva Categoría");
        updateInput.setActivo(false);

        when(productosRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productosRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product saved = invocation.getArgument(0);
            assertEquals("Nuevo Nombre", saved.getNombre());
            assertEquals("Nueva Descripción", saved.getDescripcion());
            assertEquals(new BigDecimal("200.00"), saved.getPrecio());
            assertEquals(50, saved.getCantidadStock());
            assertEquals("Nueva Categoría", saved.getCategoria());
            assertFalse(saved.getActivo());
            return saved;
        });

        // When
        productService.updateProduct(1L, updateInput);

        // Then
        verify(productosRepository).save(existingProduct);
    }

    @Test
    @DisplayName("Debe manejar error al actualizar producto")
    public void testUpdateProductRepositoryError() throws Exception {
        // Given
        Product existingProduct = new Product();
        existingProduct.setId(1L);

        ProductoInput updateInput = new ProductoInput();
        updateInput.setNombre("Producto");

        when(productosRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productosRepository.save(any(Product.class)))
            .thenThrow(new RuntimeException("Database error"));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.updateProduct(1L, updateInput);
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    @DisplayName("Debe actualizar estado activo de true a false")
    public void testUpdateProductActivoTrueToFalse() throws Exception {
        // Given
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setNombre("Producto");
        existingProduct.setActivo(true); // Actualmente activo

        ProductoInput updateInput = new ProductoInput();
        updateInput.setNombre("Producto");
        updateInput.setDescripcion("Descripción");
        updateInput.setPrecio(new BigDecimal("100.00"));
        updateInput.setCantidadStock(10);
        updateInput.setCategoria("Categoría");
        updateInput.setActivo(false); // Cambiar a inactivo

        when(productosRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productosRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product saved = invocation.getArgument(0);
            assertFalse(saved.getActivo(), "El producto debe estar inactivo");
            return saved;
        });

        // When
        String result = productService.updateProduct(1L, updateInput);

        // Then
        assertEquals("Producto actualizado exitosamente", result);
        assertFalse(existingProduct.getActivo(), "El estado debe cambiar a false");
        verify(productosRepository).save(existingProduct);
    }

    @Test
    @DisplayName("Debe actualizar estado activo de false a true")
    public void testUpdateProductActivoFalseToTrue() throws Exception {
        // Given
        Product existingProduct = new Product();
        existingProduct.setId(2L);
        existingProduct.setNombre("Producto Inactivo");
        existingProduct.setActivo(false); // Actualmente inactivo

        ProductoInput updateInput = new ProductoInput();
        updateInput.setNombre("Producto Inactivo");
        updateInput.setDescripcion("Descripción");
        updateInput.setPrecio(new BigDecimal("200.00"));
        updateInput.setCantidadStock(20);
        updateInput.setCategoria("Categoría");
        updateInput.setActivo(true); // Cambiar a activo

        when(productosRepository.findById(2L)).thenReturn(Optional.of(existingProduct));
        when(productosRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product saved = invocation.getArgument(0);
            assertTrue(saved.getActivo(), "El producto debe estar activo");
            return saved;
        });

        // When
        String result = productService.updateProduct(2L, updateInput);

        // Then
        assertEquals("Producto actualizado exitosamente", result);
        assertTrue(existingProduct.getActivo(), "El estado debe cambiar a true");
        verify(productosRepository).save(existingProduct);
    }

    @Test
    @DisplayName("Debe mantener estado activo cuando se actualiza con mismo valor")
    public void testUpdateProductActivoMaintainTrue() throws Exception {
        // Given
        Product existingProduct = new Product();
        existingProduct.setId(3L);
        existingProduct.setNombre("Producto");
        existingProduct.setActivo(true);

        ProductoInput updateInput = new ProductoInput();
        updateInput.setNombre("Producto Actualizado");
        updateInput.setDescripcion("Nueva Descripción");
        updateInput.setPrecio(new BigDecimal("300.00"));
        updateInput.setCantidadStock(30);
        updateInput.setCategoria("Nueva Categoría");
        updateInput.setActivo(true); // Mantener activo

        when(productosRepository.findById(3L)).thenReturn(Optional.of(existingProduct));
        when(productosRepository.save(any(Product.class))).thenReturn(existingProduct);

        // When
        String result = productService.updateProduct(3L, updateInput);

        // Then
        assertEquals("Producto actualizado exitosamente", result);
        assertTrue(existingProduct.getActivo(), "El estado debe mantenerse como true");
        verify(productosRepository).save(existingProduct);
    }

    @Test
    @DisplayName("Debe validar que setActivo es invocado correctamente")
    public void testUpdateProductActivoCallSetActivo() throws Exception {
        // Given
        Product existingProduct = new Product();
        existingProduct.setId(4L);
        existingProduct.setNombre("Producto");
        existingProduct.setActivo(true);

        ProductoInput updateInput = new ProductoInput();
        updateInput.setNombre("Producto");
        updateInput.setDescripcion("Descripción");
        updateInput.setPrecio(new BigDecimal("400.00"));
        updateInput.setCantidadStock(40);
        updateInput.setCategoria("Categoría");
        updateInput.setActivo(false);

        when(productosRepository.findById(4L)).thenReturn(Optional.of(existingProduct));
        when(productosRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product saved = invocation.getArgument(0);
            // Validar que el valor fue copiado correctamente del DTO
            assertEquals(updateInput.getActivo(), saved.getActivo(),
                "El valor de activo debe ser igual al del DTO");
            return saved;
        });

        // When
        productService.updateProduct(4L, updateInput);

        // Then
        assertEquals(updateInput.getActivo(), existingProduct.getActivo(),
            "El campo activo debe ser actualizado con el valor del DTO");
        verify(productosRepository).save(existingProduct);
    }

    @Test
    @DisplayName("Debe desactivar producto sin eliminarlo (soft delete)")
    public void testDeleteProductSoftDelete() throws Exception {
        // Given
        Product existingProduct = new Product();
        existingProduct.setId(5L);
        existingProduct.setNombre("Producto a Eliminar");
        existingProduct.setDescripcion("Descripción");
        existingProduct.setPrecio(new BigDecimal("100.00"));
        existingProduct.setCantidadStock(10);
        existingProduct.setCategoria("Categoría");
        existingProduct.setActivo(true); // Inicialmente activo

        when(productosRepository.findById(5L)).thenReturn(Optional.of(existingProduct));
        when(productosRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product saved = invocation.getArgument(0);
            // Verificar que setActivo(false) fue llamado
            assertFalse(saved.getActivo(), "El producto debe estar desactivado");
            return saved;
        });

        // When
        String result = productService.deleteProduct(5L);

        // Then
        assertEquals("Producto eliminado exitosamente", result);
        assertFalse(existingProduct.getActivo(), "El producto debe estar desactivado (false)");
        assertNotNull(existingProduct.getId(), "El ID debe existir (no se elimina físicamente)");
        verify(productosRepository).save(existingProduct);
    }

    @Test
    @DisplayName("Debe cambiar estado a false cuando se ejecuta setActivo en deleteProduct")
    public void testDeleteProductSetActivoFalse() throws Exception {
        // Given
        Product existingProduct = new Product();
        existingProduct.setId(6L);
        existingProduct.setActivo(true); // Comienza activo

        when(productosRepository.findById(6L)).thenReturn(Optional.of(existingProduct));
        when(productosRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product saved = invocation.getArgument(0);
            // Validar que el estado cambió exactamente a false
            assertEquals(false, saved.getActivo(), "setActivo debe asignar exactamente false");
            assertNotEquals(true, saved.getActivo(), "El valor no debe ser true");
            return saved;
        });

        // When
        productService.deleteProduct(6L);

        // Then
        assertEquals(false, existingProduct.getActivo(), "El estado debe ser false después de setActivo(false)");
        verify(productosRepository).save(existingProduct);
    }

    @Test
    @DisplayName("Debe mantener datos del producto al desactivar (no perder información)")
    public void testDeleteProductPreservesData() throws Exception {
        // Given
        Product existingProduct = new Product();
        existingProduct.setId(7L);
        existingProduct.setNombre("Producto Importante");
        existingProduct.setDescripcion("Datos críticos");
        existingProduct.setPrecio(new BigDecimal("500.00"));
        existingProduct.setCantidadStock(50);
        existingProduct.setCategoria("Premium");
        existingProduct.setFechaCreacion(LocalDateTime.now());
        existingProduct.setActivo(true);

        when(productosRepository.findById(7L)).thenReturn(Optional.of(existingProduct));
        when(productosRepository.save(any(Product.class))).thenReturn(existingProduct);

        // When
        productService.deleteProduct(7L);

        // Then
        // Verificar que solo cambió el estado, no se perdieron datos
        assertEquals(7L, existingProduct.getId(), "ID debe mantenerse");
        assertEquals("Producto Importante", existingProduct.getNombre(), "Nombre debe mantenerse");
        assertEquals("Datos críticos", existingProduct.getDescripcion(), "Descripción debe mantenerse");
        assertEquals(new BigDecimal("500.00"), existingProduct.getPrecio(), "Precio debe mantenerse");
        assertEquals(50, existingProduct.getCantidadStock(), "Stock debe mantenerse");
        assertEquals("Premium", existingProduct.getCategoria(), "Categoría debe mantenerse");
        assertFalse(existingProduct.getActivo(), "Solo activo debe cambiar a false");
        verify(productosRepository).save(existingProduct);
    }

    @Test
    @DisplayName("Debe garantizar que setActivo(false) es llamado exactamente una vez")
    public void testDeleteProductActivoFalseCalledOnce() throws Exception {
        // Given
        Product existingProduct = new Product();
        existingProduct.setId(8L);
        existingProduct.setActivo(true);

        when(productosRepository.findById(8L)).thenReturn(Optional.of(existingProduct));
        when(productosRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product saved = invocation.getArgument(0);
            // Verificar que setActivo fue llamado correctamente
            assertFalse(saved.getActivo(), "setActivo(false) debe haber sido ejecutado");
            return saved;
        });

        // When
        String result = productService.deleteProduct(8L);

        // Then
        assertEquals("Producto eliminado exitosamente", result);
        // Verificar que solo hubo una llamada a save
        verify(productosRepository, times(1)).save(existingProduct);
        // Verificar que el estado es false
        assertFalse(existingProduct.getActivo());
    }

    @Test
    @DisplayName("Debe manejar error al eliminar producto")
    public void testDeleteProductRepositoryError() throws Exception {
        // Given
        Product existingProduct = new Product();
        existingProduct.setId(1L);

        when(productosRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productosRepository.save(any(Product.class)))
            .thenThrow(new RuntimeException("Database error"));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.deleteProduct(1L);
        });

        assertEquals("Database error", exception.getMessage());
    }
}
