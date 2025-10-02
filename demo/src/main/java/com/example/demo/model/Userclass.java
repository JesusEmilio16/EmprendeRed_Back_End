package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="userclass", uniqueConstraints = {
        @UniqueConstraint( columnNames = {"email"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Userclass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(nullable=false, unique = true)
    private String email;

    private String telefono;
}
