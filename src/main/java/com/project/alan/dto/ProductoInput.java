package com.project.alan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoInput {

    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Positive(message = "El precio debe ser positivo")
    @Digits(integer = 10, fraction = 2, message = "El precio debe tener máximo 10 dígitos enteros y 2 decimales")
    @Size(max = 10, message = "El nombre debe tener entre 3 y 100 caracteres")
    private BigDecimal precio;

    @Min(value = 0, message = "La cantidad de stock no puede ser negativa")
    private Integer cantidadStock;

    @NotNull(message = "La categoría no puede ser nula")
    @NotBlank(message = "La categoría no puede estar vacía")
    private String categoria;

    private LocalDateTime fechaCreacion;

    private Boolean activo;
}
