package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "emprenderedbd", name = "business")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_business")
    private Integer idBusiness;

    // FK a usuario (propietario)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false, foreignKey = @ForeignKey(name = "fk_user"))
    @JsonIgnoreProperties({"password","hibernateLazyInitializer","handler"})
    private Usuario usuario;

    @Column(name = "name", nullable = false, length = 100)
    @NotBlank
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "address", length = 150)
    private String address;

    // FK a neighborhood
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_neighborhood", foreignKey = @ForeignKey(name = "fk_neighborhood"))
    private Neighborhood neighborhood;

    // Hay una tabla intermedia business_business_type — mapeamos ManyToMany:
    @ManyToMany
    @JoinTable(
            name = "business_business_type",
            schema = "emprenderedbd",
            joinColumns = @JoinColumn(name = "id_business"),
            inverseJoinColumns = @JoinColumn(name = "id_type")
    )
    private Set<BusinessType> types = new HashSet<>();

    @Column(name = "image1", length = 200)
    private String image1;
    @Column(name = "image2", length = 200)
    private String image2;
    @Column(name = "image3", length = 200)
    private String image3;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Getters y setters
    public Integer getIdBusiness() { return idBusiness; }
    public void setIdBusiness(Integer idBusiness) { this.idBusiness = idBusiness; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Neighborhood getNeighborhood() { return neighborhood; }
    public void setNeighborhood(Neighborhood neighborhood) { this.neighborhood = neighborhood; }

    public Set<BusinessType> getTypes() { return types; }
    public void setTypes(Set<BusinessType> types) { this.types = types; }

    public String getImage1() { return image1; }
    public void setImage1(String image1) { this.image1 = image1; }

    public String getImage2() { return image2; }
    public void setImage2(String image2) { this.image2 = image2; }

    public String getImage3() { return image3; }
    public void setImage3(String image3) { this.image3 = image3; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}