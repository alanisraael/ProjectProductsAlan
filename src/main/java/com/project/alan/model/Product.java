package com.project.alan.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "PRODUCT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false, length = 100)
    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @Column(length = 500)
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Column(precision = 10, scale = 2)
    @Positive(message = "El precio debe ser positivo")
    @Digits(integer = 10, fraction = 2, message = "El precio debe tener máximo 10 dígitos enteros y 2 decimales")
    private BigDecimal precio;

    @Column
    @Min(value = 0, message = "La cantidad de stock no puede ser negativa")
    private Integer cantidadStock;

    @Column(nullable = false)
    @NotNull(message = "La categoría no puede ser nula")
    @NotBlank(message = "La categoría no puede estar vacía")
    private String categoria;

    @Column
    private LocalDateTime fechaCreacion;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo;

    public Product(String nombre, String descripcion, BigDecimal precio, Integer cantidadStock, String categoria, LocalDateTime fechaCreacion, Boolean activo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidadStock = cantidadStock;
        this.categoria = categoria;
        this.fechaCreacion = fechaCreacion;
        this.activo = activo;
    }
}
