package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desactiva protección CSRF (necesario para POST/PUT/DELETE)
                .authorizeHttpRequests(auth -> auth
                        // 🔓 ESTA LÍNEA ES LA CLAVE:
                        // Permite el acceso a CUALQUIER ruta, con CUALQUIER método (GET, POST, etc.)
                        // a CUALQUIER usuario (sin necesidad de login).
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}