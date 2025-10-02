package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserClassResponse {
    private int idUser;
    private String name;
    private String middleName;
    private String lastName;
    private String lastName2;
    private String email;
    private String password;
    private String phoneNumber;
    private String documento;
    private String sexo;
}


