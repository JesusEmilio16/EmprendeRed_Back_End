package com.example.demo.dto;

import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BusinessRequest {
    private String nombreUsuario;
    private String name;
    private String direccion;
    private String barrio;
    private String description;

    private MultipartFile image;
}
