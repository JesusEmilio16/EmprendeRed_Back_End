package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;

@Entity
@Table(schema = "emprenderedbd", name = "business_type")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BusinessType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type")
    private Long idType;

    @Column(name = "name", nullable = false, length = 50, unique = true)
    @NotBlank
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    // Getters y setters
    public Long getIdType() { return idType; }
    public void setIdType(Long idType) { this.idType = idType; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}