package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.service.UsuarioService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
@Hidden
public class AuthController {
    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Llama al metodo login del servicio
            LoginResponse response = usuarioService.login(request);
            return ResponseEntity.ok(response);
        }
        catch (org.springframework.web.server.ResponseStatusException ex) {
            // Si lanza error de credenciales inválidas
            return ResponseEntity.status(ex.getStatusCode())
                    .body(java.util.Map.of("message", ex.getReason()));
        }
        catch (Exception ex) {
            // Si ocurre cualquier otro error inesperado
            return ResponseEntity.status(500)
                    .body(java.util.Map.of("message", "Error interno del servidor"));
        }
    }
}
