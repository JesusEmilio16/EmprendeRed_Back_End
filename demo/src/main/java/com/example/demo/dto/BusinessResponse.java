package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessResponse {
    private Long idBusiness;
    private String nombreUsuario;
    private String name;
    private String direccion;
    private String barrio;
    private String description;
    private String imagePath;
    private String createdAt;
    private Boolean isActive;
}
