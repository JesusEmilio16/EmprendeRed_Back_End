package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCommentResponse {
    private Long id;
    private String content;
    private Timestamp createdAt;
    private BigDecimal rating;

    // Información del usuario
    private Integer userId;
    private String userName;
    private String userLastName;

    // Información del negocio
    private Long businessId;
    private String businessName;
}