package com.project.alan.services;

import com.project.alan.dto.ProductoInput;
import com.project.alan.model.Product;
import com.project.alan.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productosRepository;

    @Autowired
    public ProductService(ProductRepository productosRepository) {
        this.productosRepository = productosRepository;
    }

    public String createProduct(ProductoInput newProductoInput) throws Exception {
        Product newProduct = new Product();
        newProduct.setNombre(newProductoInput.getNombre());
        newProduct.setDescripcion(newProductoInput.getDescripcion());
        newProduct.setPrecio(newProductoInput.getPrecio());
        newProduct.setCantidadStock(newProductoInput.getCantidadStock());
        newProduct.setCategoria(newProductoInput.getCategoria());
        newProduct.setFechaCreacion(new java.util.Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
        newProduct.setActivo(true);

        productosRepository.save(newProduct);
        if (newProduct.getId() == null) {
            return "Error al crear el producto";
        }
        return "Producto creado exitosamente";
    }

    public String updateProduct(Long id, ProductoInput productoInput) throws Exception {

        Optional<Product> productOptional = productosRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + " not available");
        }

        Product product = productOptional.get();
        product.setNombre(productoInput.getNombre());
        product.setDescripcion(productoInput.getDescripcion());
        product.setPrecio(productoInput.getPrecio());
        product.setCantidadStock(productoInput.getCantidadStock());
        product.setCategoria(productoInput.getCategoria());
        product.setActivo(productoInput.getActivo());

        productosRepository.save(product);
        if (product.getId() == null) {
            return "Error al actualizar el producto";
        }
        return "Producto actualizado exitosamente";

    }

    public String deleteProduct(Long id) throws Exception {

        Optional<Product> productOptional = productosRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + " not available");
        }

        Product product = productOptional.get();
        product.setActivo(false);
        productosRepository.save(product);
        if (product.getId() == null) {
            return "Error al eliminar el producto";
        }
        return "Producto eliminado exitosamente";
    }



    }
