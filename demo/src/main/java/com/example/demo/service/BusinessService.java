package com.example.demo.service;

import com.example.demo.dto.BusinessRequest;
import com.example.demo.dto.BusinessResponse;
import com.example.demo.model.Business;
import com.example.demo.model.Usuario;
import com.example.demo.repository.BusinessRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessService {

    private final BusinessRepository businessRepo;
    private final UsuarioRepository usuarioRepo;

    // Ruta donde se guardarán las imágenes (relativa al proyecto)
    @Value("${upload.path:uploads/}")
    private String uploadPath;

    public BusinessService(BusinessRepository businessRepo, UsuarioRepository usuarioRepo) {
        this.businessRepo = businessRepo;
        this.usuarioRepo = usuarioRepo;
    }

    // 🟢 Crear negocio asociado al usuario logueado
    public BusinessResponse create(Long userId, BusinessRequest request) {
        Usuario usuario = usuarioRepo.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + userId));

        Business entity = new Business();
        entity.setUsuario(usuario);
        entity.setName(request.getName());
        entity.setDireccion(request.getDireccion());
        entity.setBarrio(request.getBarrio());
        entity.setDescription(request.getDescription());
        entity.setIsActive(true);
        entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        // Guardar imagen si existe
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            entity.setImagePath(saveImage(request.getImage()));
        }

        Business saved = businessRepo.save(entity);
        return toResponse(saved);
    }

    // 🟡 Obtener negocios del usuario logueado
    public List<BusinessResponse> getByUser(Long userId) {
        return businessRepo.findByUsuario_IdUser(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 🟣 Obtener todos los negocios (público)
    public List<BusinessResponse> getAll() {
        return businessRepo.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 🟠 Actualizar negocio (solo si pertenece al usuario)
    public BusinessResponse update(Long id, Long userId, BusinessRequest request) {
        Business entity = businessRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado con id: " + id));

        if (!entity.getUsuario().getIdUser().equals(userId)) {
            throw new RuntimeException("No tienes permiso para modificar este negocio.");
        }

        entity.setName(request.getName());
        entity.setDireccion(request.getDireccion());
        entity.setBarrio(request.getBarrio());
        entity.setDescription(request.getDescription());

        // Reemplazar imagen si se sube una nueva
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            entity.setImagePath(saveImage(request.getImage()));
        }

        Business updated = businessRepo.save(entity);
        return toResponse(updated);
    }

    // 🔴 Eliminar negocio (solo si pertenece al usuario)
    public void delete(Long id, Long userId) {
        System.out.println( "este es el usuario service a eliminar"+userId);
        System.out.println( "este es el negocio service a eliminar"+id);
        Business entity = businessRepo.findById(id)

                .orElseThrow(() -> new RuntimeException("Negocio no encontrado con id: " + id));

        // Validación segura
        if (entity.getUsuario() == null) {
            throw new RuntimeException("El negocio no tiene usuario asociado.");
        }

        Long ownerId = entity.getUsuario().getIdUser().longValue();


        if (!ownerId.equals(userId)) {
            System.out.println( "este es el negocio service 2 a eliminar"+ownerId);
            System.out.println( "este es el ususario service 2 a eliminar"+userId);
            throw new RuntimeException("No tienes permiso para eliminar este negocio bl bla.");
        }

        businessRepo.delete(entity);
    }

    // 💾 Guardar imagen en carpeta "uploads" y devolver URL accesible
    private String saveImage(MultipartFile file) {
        if (file.isEmpty()) return null;

        try {
            Path uploadDir = Paths.get(uploadPath);

            // Crear carpeta si no existe
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Nombre único para la imagen
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);

            // Guardar archivo
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Devuelve URL accesible públicamente (relativa al backend)
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar imagen: " + e.getMessage());
        }
    }

    // 🧱 Convertir entidad a DTO
    private BusinessResponse toResponse(Business entity) {
        return new BusinessResponse(
                entity.getIdBusiness(),
                entity.getUsuario().getName(),
                entity.getName(),
                entity.getDireccion(),
                entity.getBarrio(),
                entity.getDescription(),
                entity.getImagePath(),
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null,
                entity.getIsActive()
        );
    }
}
