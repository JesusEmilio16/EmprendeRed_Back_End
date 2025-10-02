package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(schema = "emprenderedbd", name = "neighborhood")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Neighborhood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_neighborhood")
    private Integer idNeighborhood;

    @Column(name = "name", nullable = false, length = 100, unique = true)
    @NotBlank
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    // Getters y setters
    public Integer getIdNeighborhood() { return idNeighborhood; }
    public void setIdNeighborhood(Integer idNeighborhood) { this.idNeighborhood = idNeighborhood; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}