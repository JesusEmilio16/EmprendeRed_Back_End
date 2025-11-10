package com.example.demo.service;

import com.example.demo.dto.BusinessRequest;
import com.example.demo.dto.BusinessResponse;
import com.example.demo.model.Business;
import com.example.demo.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessService {

    private final BusinessRepository repo;

    // Ruta base donde se guardarán las imágenes
    @Value("${upload.path:uploads/}")
    private String uploadPath;

    public BusinessService(BusinessRepository repo) {
        this.repo = repo;
    }

    // Crear un nuevo negocio
    public BusinessResponse create(BusinessRequest request) {
        Business entity = new Business();
        entity.setNombreUsuario(request.getNombreUsuario());
        entity.setName(request.getName());
        entity.setDireccion(request.getDireccion());
        entity.setBarrio(request.getBarrio());
        entity.setDescription(request.getDescription());
        entity.setIsActive(true);
        entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        // Guardar imagen si existe
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String path = saveImage(request.getImage());
            entity.setImagePath(path);
        }

        Business saved = repo.save(entity);
        return toResponse(saved);
    }

    // Obtener todos los negocios
    public List<BusinessResponse> getAll() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Obtener por ID
    public BusinessResponse getById(Long id) {
        Business entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado: " + id));
        return toResponse(entity);
    }

    // Actualizar negocio
    public BusinessResponse update(Long id, BusinessRequest request) {
        Business entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado: " + id));

        entity.setNombreUsuario(request.getNombreUsuario());
        entity.setName(request.getName());
        entity.setDireccion(request.getDireccion());
        entity.setBarrio(request.getBarrio());
        entity.setDescription(request.getDescription());

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String path = saveImage(request.getImage());
            entity.setImagePath(path);
        }

        Business saved = repo.save(entity);
        return toResponse(saved);
    }

    // Eliminar negocio
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Negocio no encontrado: " + id);
        }
        repo.deleteById(id);
    }

    // Guardar imagen en disco y devolver la ruta
    private String saveImage(MultipartFile file) {

        if (file.isEmpty()) {
            return null;
        }

        try {
            File dir = new File(uploadPath);
            if (!dir.exists()) dir.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destination = new File(dir.getAbsolutePath() + File.separator + fileName);
            /*String fullPath = uploadPath + fileName;*/
            file.transferTo(destination);
            return "http://localhost:8080/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar imagen: " + e.getMessage());
        }
    }

    private BusinessResponse toResponse(Business entity) {
        return new BusinessResponse(
                entity.getIdBusiness(),
                entity.getNombreUsuario(),
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
