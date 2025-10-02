package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserClassResponse {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
}


