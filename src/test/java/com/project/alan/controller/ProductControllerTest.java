package com.project.alan.controller;

import com.project.alan.dto.ProductoInput;
import com.project.alan.model.Product;
import com.project.alan.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ProductController Tests")
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productosRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product producto1;
    private Product producto2;

    @BeforeEach
    public void setUp() {
        productosRepository.deleteAll();

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

        productosRepository.saveAll(List.of(producto1, producto2));
    }

    @Test
    @DisplayName("GET /productos - Debe retornar lista de productos exitosamente")
    public void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/productos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
            .andExpect(jsonPath("$[0].nombre", notNullValue()))
            .andExpect(jsonPath("$[0].precio", notNullValue()))
            .andExpect(jsonPath("$[0].categoria", notNullValue()));
    }

    @Test
    @DisplayName("GET /productos - Debe retornar 204 cuando no hay productos")
    public void testGetAllProductsNoContent() throws Exception {
        // Limpiar repositorio
        productosRepository.deleteAll();

        mockMvc.perform(get("/productos"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /productos - Debe retornar todos los campos del producto")
    public void testGetAllProductsReturnAllFields() throws Exception {
        mockMvc.perform(get("/productos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", notNullValue()))
            .andExpect(jsonPath("$[0].nombre", is("Laptop HP")))
            .andExpect(jsonPath("$[0].descripcion", is("Laptop de 15 pulgadas")))
            .andExpect(jsonPath("$[0].precio", is(999.99)))
            .andExpect(jsonPath("$[0].cantidadStock", is(50)))
            .andExpect(jsonPath("$[0].categoria", is("Electrónica")))
            .andExpect(jsonPath("$[0].activo", is(true)));
    }

    @Test
    @DisplayName("GET /productos - Debe retornar lista en formato JSON array")
    public void testGetAllProductsJsonFormat() throws Exception {
        mockMvc.perform(get("/productos"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", isA(java.util.ArrayList.class)));
    }

    @Test
    @DisplayName("GET /productos/{id} - Debe retornar producto por ID")
    public void testGetProductById() throws Exception {
        Long id = producto1.getId();
        mockMvc.perform(get("/productos/" + id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre", is("Laptop HP")))
            .andExpect(jsonPath("$.precio", is(999.99)));
    }

    @Test
    @DisplayName("GET /productos/{id} - Debe retornar 404 si no existe")
    public void testGetProductByIdNotFound() throws Exception {
        mockMvc.perform(get("/productos/9999"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /productos/activos - Debe retornar solo productos activos")
    public void testGetActiveProducts() throws Exception {
        mockMvc.perform(get("/productos/activos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
            .andExpect(jsonPath("$[0].activo", is(true)))
            .andExpect(jsonPath("$[*].activo", everyItem(is(true))));
    }

    @Test
    @DisplayName("GET /productos/activos - Debe retornar 204 cuando no hay productos activos")
    public void testGetActiveProductsNoContent() throws Exception {
        // Desactivar todos los productos
        producto1.setActivo(false);
        producto2.setActivo(false);
        productosRepository.saveAll(List.of(producto1, producto2));

        mockMvc.perform(get("/productos/activos"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /productos/activos - Debe retornar solo productos con activo=true")
    public void testGetActiveProductsFilterCorrectly() throws Exception {
        // Crear un producto inactivo
        Product productoInactivo = new Product(
            "Producto Inactivo",
            "Descripción",
            new BigDecimal("100.00"),
            10,
            "Categoría",
            LocalDateTime.now(),
            false
        );
        productosRepository.save(productoInactivo);

        // Obtener productos activos
        mockMvc.perform(get("/productos/activos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2))) // Solo los 2 activos
            .andExpect(jsonPath("$[*].activo", everyItem(is(true))));
    }

    @Test
    @DisplayName("GET /productos/activos - Debe retornar todos los campos del producto activo")
    public void testGetActiveProductsReturnAllFields() throws Exception {
        mockMvc.perform(get("/productos/activos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", notNullValue()))
            .andExpect(jsonPath("$[0].nombre", notNullValue()))
            .andExpect(jsonPath("$[0].descripcion", notNullValue()))
            .andExpect(jsonPath("$[0].precio", notNullValue()))
            .andExpect(jsonPath("$[0].cantidadStock", notNullValue()))
            .andExpect(jsonPath("$[0].categoria", notNullValue()))
            .andExpect(jsonPath("$[0].activo", is(true)));
    }

    @Test
    @DisplayName("GET /productos/categoria - Debe filtrar por categoría exitosamente")
    public void testGetProductsByCategoria() throws Exception {
        mockMvc.perform(get("/productos/categoria?categoria=Electrónica"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
            .andExpect(jsonPath("$[0].categoria", is("Electrónica")))
            .andExpect(jsonPath("$[0].nombre", notNullValue()));
    }

    @Test
    @DisplayName("GET /productos/categoria - Debe retornar 204 si no hay productos en la categoría")
    public void testGetProductsByCategoriaNotFound() throws Exception {
        mockMvc.perform(get("/productos/categoria?categoria=NoExiste"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /productos/categoria - Debe filtrar correctamente por categoría específica")
    public void testGetProductsByCategoriaFiltering() throws Exception {
        // Crear un producto en una categoría diferente
        Product producto3 = new Product(
            "Mouse Logitech",
            "Mouse inalámbrico",
            new BigDecimal("49.99"),
            100,
            "Accesorios",
            LocalDateTime.now(),
            true
        );
        productosRepository.save(producto3);

        // Buscar por categoría "Accesorios"
        mockMvc.perform(get("/productos/categoria?categoria=Accesorios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].nombre", is("Mouse Logitech")))
            .andExpect(jsonPath("$[0].categoria", is("Accesorios")));
    }



    @Test
    @DisplayName("GET /productos/precio - Debe retornar 204 si no hay productos en rango")
    public void testGetProductsByPriceRangeNoContent() throws Exception {
        mockMvc.perform(get("/productos/precio?min=0.01&max=50"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /productos/precio - Debe filtrar correctamente por rango exacto")
    public void testGetProductsByPriceRangeExact() throws Exception {
        // Crear productos en diferentes rangos
        Product productoBarato = new Product(
            "Mouse Económico",
            "Mouse básico",
            new BigDecimal("25.00"),
            50,
            "Accesorios",
            LocalDateTime.now(),
            true
        );
        Product productoMedio = new Product(
            "Teclado Premium",
            "Teclado mecánico",
            new BigDecimal("150.00"),
            30,
            "Accesorios",
            LocalDateTime.now(),
            true
        );
        productosRepository.saveAll(List.of(productoBarato, productoMedio));

    }

    @Test
    @DisplayName("GET /productos/precio - Debe retornar todos los campos en rango de precio")
    public void testGetProductsByPriceRangeReturnAllFields() throws Exception {
        mockMvc.perform(get("/productos/precio?min=100&max=1000"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", notNullValue()))
            .andExpect(jsonPath("$[0].nombre", notNullValue()))
            .andExpect(jsonPath("$[0].precio", notNullValue()))
            .andExpect(jsonPath("$[0].categoria", notNullValue()));
    }

    @Test
    @DisplayName("POST /productos - Debe crear un nuevo producto (autenticado)")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateProduct() throws Exception {
        ProductoInput productoInput = new ProductoInput();
        productoInput.setNombre("Teclado Mecánico");
        productoInput.setDescripcion("Teclado RGB");
        productoInput.setPrecio(new BigDecimal("149.99"));
        productoInput.setCantidadStock(100);
        productoInput.setCategoria("Accesorios");
        productoInput.setActivo(true);

        mockMvc.perform(post("/productos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productoInput)))
            .andExpect(status().isCreated())
            .andExpect(content().string(containsString("exitosamente")));
    }

    @Test
    @DisplayName("POST /productos - Debe rechazar sin autenticación")
    public void testCreateProductUnauthorized() throws Exception {
        ProductoInput productoInput = new ProductoInput();
        productoInput.setNombre("Producto Test");
        productoInput.setDescripcion("Descripción");
        productoInput.setPrecio(new BigDecimal("100.00"));
        productoInput.setCantidadStock(50);
        productoInput.setCategoria("Test");

        mockMvc.perform(post("/productos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productoInput)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /productos - Debe validar nombre no nulo")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateProductNullNombre() throws Exception {
        ProductoInput productoInput = new ProductoInput();
        productoInput.setNombre(null);
        productoInput.setDescripcion("Descripción");
        productoInput.setPrecio(new BigDecimal("100.00"));
        productoInput.setCantidadStock(50);
        productoInput.setCategoria("Categoria");

        mockMvc.perform(post("/productos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productoInput)))
            .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("PUT /productos/{id} - Debe actualizar un producto (autenticado)")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testUpdateProduct() throws Exception {
        Long id = producto1.getId();
        ProductoInput productoInput = new ProductoInput();
        productoInput.setNombre("Laptop Actualizada");
        productoInput.setDescripcion("Laptop actualizada de 15 pulgadas");
        productoInput.setPrecio(new BigDecimal("1299.99"));
        productoInput.setCantidadStock(40);
        productoInput.setCategoria("Electrónica");
        productoInput.setActivo(true);

        mockMvc.perform(put("/productos/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productoInput)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("exitosamente")));
    }

    @Test
    @DisplayName("PUT /productos/{id} - Debe retornar 400 si producto no existe")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testUpdateProductNotFound() throws Exception {
        ProductoInput productoInput = new ProductoInput();
        productoInput.setNombre("Producto");
        productoInput.setDescripcion("Descripción");
        productoInput.setPrecio(new BigDecimal("100.00"));
        productoInput.setCantidadStock(50);
        productoInput.setCategoria("Categoria");

        mockMvc.perform(put("/productos/9999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productoInput)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /productos/{id} - Debe rechazar sin autenticación")
    public void testUpdateProductUnauthorized() throws Exception {
        Long id = producto1.getId();
        ProductoInput productoInput = new ProductoInput();
        productoInput.setNombre("Laptop");

        mockMvc.perform(put("/productos/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productoInput)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("PUT /productos/{id} - Debe actualizar todos los campos")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testUpdateProductUpdatesAllFields() throws Exception {
        Long id = producto1.getId();
        ProductoInput productoInput = new ProductoInput();
        productoInput.setNombre("Nuevo Nombre");
        productoInput.setDescripcion("Nueva Descripción");
        productoInput.setPrecio(new BigDecimal("555.55"));
        productoInput.setCantidadStock(25);
        productoInput.setCategoria("Nueva Categoría");
        productoInput.setActivo(false);

        mockMvc.perform(put("/productos/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productoInput)))
            .andExpect(status().isOk());

        // Verificar que se actualizaron
        var productoOpt = productosRepository.findById(id);
        assertTrue(productoOpt.isPresent());
        Product actualizado = productoOpt.get();
        assertEquals("Nuevo Nombre", actualizado.getNombre());
        assertEquals("Nueva Descripción", actualizado.getDescripcion());
        assertEquals(new BigDecimal("555.55"), actualizado.getPrecio());
        assertEquals(25, actualizado.getCantidadStock());
        assertEquals("Nueva Categoría", actualizado.getCategoria());
        assertFalse(actualizado.getActivo());
    }

    @Test
    @DisplayName("PUT /productos/{id} - Debe validar precio positivo en actualización")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testUpdateProductInvalidPrice() throws Exception {
        Long id = producto1.getId();
        ProductoInput productoInput = new ProductoInput();
        productoInput.setNombre("Producto");
        productoInput.setDescripcion("Descripción");
        productoInput.setPrecio(new BigDecimal("-100.00"));
        productoInput.setCantidadStock(50);
        productoInput.setCategoria("Categoria");

        mockMvc.perform(put("/productos/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productoInput)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /productos/{id} - Debe desactivar un producto (autenticado)")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testDeleteProduct() throws Exception {
        Long id = producto1.getId();

        mockMvc.perform(delete("/productos/" + id))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("exitosamente")));
    }

    @Test
    @DisplayName("DELETE /productos/{id} - Debe retornar 404 si producto no existe")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testDeleteProductNotFound() throws Exception {
        mockMvc.perform(delete("/productos/9999"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /productos/{id} - Debe rechazar sin autenticación")
    public void testDeleteProductUnauthorized() throws Exception {
        Long id = producto1.getId();

        mockMvc.perform(delete("/productos/" + id))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("DELETE /productos/{id} - Debe marcar producto como inactivo")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testDeleteProductMarksInactive() throws Exception {
        Long id = producto1.getId();

        // Verificar que está activo
        var prodActOpt = productosRepository.findById(id);
        assertTrue(prodActOpt.isPresent());
        assertTrue(prodActOpt.get().getActivo());

        // Eliminar (desactivar)
        mockMvc.perform(delete("/productos/" + id))
            .andExpect(status().isOk());

        // Verificar que está inactivo
        var prodInactOpt = productosRepository.findById(id);
        assertTrue(prodInactOpt.isPresent());
        assertFalse(prodInactOpt.get().getActivo());
    }

    @Test
    @DisplayName("DELETE /productos/{id} - Debe validar formato de ID")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testDeleteProductInvalidId() throws Exception {
        mockMvc.perform(delete("/productos/abc"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /productos - Debe fallar si categoría es nula")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateProductInvalidCategoria() throws Exception {
        ProductoInput productoInput = new ProductoInput();
        productoInput.setNombre("Producto");
        productoInput.setDescripcion("Descripción");
        productoInput.setPrecio(new BigDecimal("100.00"));
        productoInput.setCantidadStock(50);
        productoInput.setCategoria(null);

        mockMvc.perform(post("/productos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productoInput)))
            .andExpect(status().isBadRequest());
    }
}
