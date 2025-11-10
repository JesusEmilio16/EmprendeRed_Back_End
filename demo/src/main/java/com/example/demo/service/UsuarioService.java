package com.example.demo.service;

import com.example.demo.dto.UserclassRequest;
import com.example.demo.dto.UserClassResponse;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
//login
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Constructor: inyecta repo + passwordEncoder
    public UsuarioService(UsuarioRepository repo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // traer usuarios (no devolvemos password)
    public List<UserClassResponse> lista() {
        return repo.findAll()
                .stream()
                .map(u -> new UserClassResponse(
                        u.getIdUser(),
                        u.getName(),
                        u.getMiddleName(),
                        u.getLastName(),
                        u.getLastName2(),
                        u.getEmail(),
                        null, // NO devolver password
                        u.getPhoneNumber(),
                        u.getDocumento(),
                        u.getSexo()
                ))
                .collect(Collectors.toList());
    }

    // crear usuario (ENCRIPTA contraseña)
    public UserClassResponse create(UserclassRequest request) {
        Usuario entity = new Usuario();
        entity.setName(request.getName());
        entity.setMiddleName(request.getMiddleName());
        entity.setLastName(request.getLastName());
        entity.setLastName2(request.getLastName2());
        entity.setEmail(request.getEmail());
        // ENCRIPTAR la contraseña antes de guardar
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setPhoneNumber(request.getPhoneNumber());
        entity.setDocumento(request.getDocumento());
        entity.setSexo(request.getSexo());


        Usuario saved = repo.save(entity);
        return new UserClassResponse(
                saved.getIdUser(),
                saved.getName(),
                saved.getMiddleName(),
                saved.getLastName(),
                saved.getLastName2(),
                saved.getEmail(),
                null, // NO devolver password
                saved.getPhoneNumber(),
                saved.getDocumento(),
                saved.getSexo()
        );
    }

    // listar por id (no devolvemos password)
    public UserClassResponse findById(Integer idUser) {
        Usuario entity = repo.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado " + idUser));
        return new UserClassResponse(
                entity.getIdUser(),
                entity.getName(),
                entity.getMiddleName(),
                entity.getLastName(),
                entity.getLastName2(),
                entity.getEmail(),
                null, // NO devolver password
                entity.getPhoneNumber(),
                entity.getDocumento(),
                entity.getSexo()
        );
    }

    // actualizar usuario (si viene password la encriptamos)
    public UserClassResponse update(Integer idUser, UserclassRequest request) {
        Usuario entity = repo.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + idUser));
        entity.setName(request.getName());
        entity.setMiddleName(request.getMiddleName());
        entity.setLastName(request.getLastName());
        entity.setLastName2(request.getLastName2());
        entity.setEmail(request.getEmail());

        // Si llega password en el body, la encriptamos y actualizamos
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            entity.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        entity.setPhoneNumber(request.getPhoneNumber());
        entity.setDocumento(request.getDocumento());
        entity.setSexo(request.getSexo());

        Usuario saved = repo.save(entity);
        return new UserClassResponse(
                saved.getIdUser(),
                saved.getName(),
                saved.getMiddleName(),
                saved.getLastName(),
                saved.getLastName2(),
                saved.getEmail(),
                null, // NO devolver password
                saved.getPhoneNumber(),
                saved.getDocumento(),
                saved.getSexo()
        );
    }

    public void delete(Integer id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id " + id);
        }
        repo.deleteById(id);
    }

    public LoginResponse login(LoginRequest request) {
        // Buscar usuario por email
        Usuario user = repo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        // Comparar contraseña (plaintext vs hashed)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        System.out.println("ID: " + user.getIdUser());
        // Generar token: subject = idUser (string) o email, según prefieras
        String token = jwtUtil.generateToken(String.valueOf(user.getIdUser()));


        // Devolver DTO con token
        return new LoginResponse(
                user.getIdUser(),
                user.getName(),
                user.getEmail(),
                token
        );
    }


}