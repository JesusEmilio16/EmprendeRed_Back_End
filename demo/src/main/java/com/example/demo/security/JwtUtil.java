package com.example.demo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    // ✅ SECRET FIJA PARA PRUEBAS (puede ser cualquier string de mínimo 32 chars)
    private static final String SECRET = "esta_es_una_clave_de_prueba_muy_larga_123456";

    // ✅ Convertimos la string en una key válida
    private final Key secretKey = new SecretKeySpec(
            SECRET.getBytes(),
            SignatureAlgorithm.HS256.getJcaName()
    );

    // Duración del token: 1 hora
    private final long EXPIRATION_TIME = 3600000;

    //  Generar token
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    //  Validar token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
