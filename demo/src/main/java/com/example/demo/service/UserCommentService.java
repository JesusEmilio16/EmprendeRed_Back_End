package com.example.demo.service;

import com.example.demo.dto.UserCommentRequest;
import com.example.demo.dto.UserCommentResponse;
import com.example.demo.model.Business;
import com.example.demo.model.UserComment;
import com.example.demo.model.Usuario;
import com.example.demo.repository.BusinessRepository;
import com.example.demo.repository.UserCommentRepository;
import com.example.demo.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCommentService {

    @Autowired
    private UserCommentRepository commentRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BusinessRepository businessRepository;

    // Convertir entidad a DTO Response
    private UserCommentResponse convertToResponse(UserComment comment) {
        UserCommentResponse response = new UserCommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt());
        response.setRating(comment.getRating());

        // Datos del usuario (Integer a Integer - sin conversión)
        response.setUserId(comment.getUser().getIdUser());
        response.setUserName(comment.getUser().getName());
        response.setUserLastName(comment.getUser().getLastName());

        // Datos del negocio (Long a Long - sin conversión)
        response.setBusinessId(comment.getBusiness().getIdBusiness());
        response.setBusinessName(comment.getBusiness().getName());

        return response;
    }

    // Obtener todos los comentarios
    @Transactional(readOnly = true)
    public List<UserCommentResponse> getAllComments() {
        return commentRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Obtener comentario por ID
    @Transactional(readOnly = true)
    public UserCommentResponse getCommentById(Long id) {
        UserComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comentario no encontrado con ID: " + id));
        return convertToResponse(comment);
    }

    // Obtener comentarios por negocio
    @Transactional(readOnly = true)
    public List<UserCommentResponse> getCommentsByBusiness(Long businessId) {
        return commentRepository.findByBusinessIdBusinessOrderByCreatedAtDesc(businessId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Obtener comentarios por usuario
    @Transactional(readOnly = true)
    public List<UserCommentResponse> getCommentsByUser(Integer userId) {
        return commentRepository.findByUserIdUserOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Crear comentario
    @Transactional
    public UserCommentResponse createComment(UserCommentRequest request) {
        // Buscar usuario
        Usuario usuario = usuarioRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + request.getUserId()));

        // Buscar negocio
        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new EntityNotFoundException("Negocio no encontrado con ID: " + request.getBusinessId()));

        // Crear comentario
        UserComment comment = new UserComment();
        comment.setContent(request.getContent());
        comment.setRating(request.getRating());
        comment.setUser(usuario);
        comment.setBusiness(business);

        UserComment savedComment = commentRepository.save(comment);
        return convertToResponse(savedComment);
    }

    // Actualizar comentario
    @Transactional
    public UserCommentResponse updateComment(Long id, UserCommentRequest request) {
        UserComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comentario no encontrado con ID: " + id));

        comment.setContent(request.getContent());
        comment.setRating(request.getRating());

        UserComment updatedComment = commentRepository.save(comment);
        return convertToResponse(updatedComment);
    }

    // Eliminar comentario
    @Transactional
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new EntityNotFoundException("Comentario no encontrado con ID: " + id);
        }
        commentRepository.deleteById(id);
    }

    // Obtener promedio de rating de un negocio
    @Transactional(readOnly = true)
    public Double getAverageRating(Long businessId) {
        Double average = commentRepository.getAverageRatingByBusiness(businessId);
        return average != null ? average : 0.0;
    }

    // Contar comentarios por negocio
    @Transactional(readOnly = true)
    public long countCommentsByBusiness(Long businessId) {
        return commentRepository.countByBusinessIdBusiness(businessId);
    }






}


