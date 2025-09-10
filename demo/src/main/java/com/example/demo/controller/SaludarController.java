package com.example.demo.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaludarController {
    @GetMapping("/saludo")
    public String saludo(String nombre) {
        return "Saludo: " + nombre+ "bienvenido a mi controlador";
    }
}
