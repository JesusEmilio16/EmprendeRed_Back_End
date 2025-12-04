package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server; // 👈 Importante
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API EmprendeRed")
                        .version("1.0")
                        .description("Documentación pública de la API de EmprendeRed.")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                // 👇 ESTO ES LO NUEVO: Definimos los servidores
                .servers(List.of(
                        new Server().url("https://emprenderedbackend-production.up.railway.app").description("Servidor de Producción (Railway)"),
                        new Server().url("http://localhost:8080").description("Servidor Local")
                ));
    }
}