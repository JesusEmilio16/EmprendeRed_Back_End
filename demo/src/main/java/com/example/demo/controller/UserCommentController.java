package com.example.demo.controller;

import com.example.demo.dto.UserCommentRequest;
import com.example.demo.dto.UserCommentResponse;
import com.example.demo.service.UserCommentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "http://localhost:4200")
public class UserCommentController {

    @Autowired
    private UserCommentService commentService;

    // Obtener todos los comentarios
    @GetMapping
    public ResponseEntity<List<UserCommentResponse>> getAllComments() {
        List<UserCommentResponse> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    // Obtener comentario por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id) {
        try {
            UserCommentResponse comment = commentService.getCommentById(id);
            return ResponseEntity.ok(comment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Obtener comentarios por negocio
    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<UserCommentResponse>> getCommentsByBusiness(@PathVariable Long businessId) {
        List<UserCommentResponse> comments = commentService.getCommentsByBusiness(businessId);
        return ResponseEntity.ok(comments);
    }

    // Obtener comentarios por usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserCommentResponse>> getCommentsByUser(@PathVariable Integer userId) {
        List<UserCommentResponse> comments = commentService.getCommentsByUser(userId);
        return ResponseEntity.ok(comments);
    }

    // Crear comentario
    @PostMapping
    public ResponseEntity<?> createComment(@Valid @RequestBody UserCommentRequest request) {
        try {
            UserCommentResponse createdComment = commentService.createComment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Actualizar comentario
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody UserCommentRequest request) {
        try {
            UserCommentResponse updatedComment = commentService.updateComment(id, request);
            return ResponseEntity.ok(updatedComment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Eliminar comentario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok(Map.of("message", "Comentario eliminado exitosamente"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Obtener promedio de rating de un negocio
    @GetMapping("/business/{businessId}/average-rating")
    public ResponseEntity<Map<String, Object>> getAverageRating(@PathVariable Long businessId) {
        Double average = commentService.getAverageRating(businessId);
        long count = commentService.countCommentsByBusiness(businessId);

        Map<String, Object> response = new HashMap<>();
        response.put("businessId", businessId);
        response.put("averageRating", average);
        response.put("totalComments", count);

        return ResponseEntity.ok(response);
    }
}