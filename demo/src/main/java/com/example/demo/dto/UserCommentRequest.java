package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCommentRequest {

    @NotBlank(message = "El contenido del comentario no puede estar vacío")
    @Size(min = 10, max = 1000, message = "El comentario debe tener entre 10 y 1000 caracteres")
    private String content;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer userId;  // ⬅️ Mantén como Integer

    @NotNull(message = "El ID del negocio es obligatorio")
    private Long businessId;  // ⬅️ Mantén como Long

    @NotNull(message = "La calificación es obligatoria")
    @DecimalMin(value = "0.0", message = "La calificación mínima es 0.0")
    @DecimalMax(value = "5.0", message = "La calificación máxima es 5.0")
    private BigDecimal rating;
}