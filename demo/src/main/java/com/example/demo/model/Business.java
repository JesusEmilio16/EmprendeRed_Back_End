package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(schema = "emprenderedbd", name = "business")
@Data
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_business")
    private Long idBusiness;

    @Column(name = "nombre_usuario", nullable = false, length = 100)
    private String nombreUsuario;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @Column(name = "barrio", nullable = false, length = 100)
    private String barrio;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @Column(name = "image_path", length = 300)
    private String imagePath;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private java.sql.Timestamp createdAt;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;
}
