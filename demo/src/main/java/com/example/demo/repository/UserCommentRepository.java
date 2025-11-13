package com.example.demo.repository;

import com.example.demo.model.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCommentRepository extends JpaRepository<UserComment, Long> {

    // Encontrar todos los comentarios de un negocio
    // business.idBusiness se refiere a la propiedad idBusiness de la entidad Business
    List<UserComment> findByBusinessIdBusinessOrderByCreatedAtDesc(Long businessId);

    // Encontrar todos los comentarios de un usuario
    // user.idUser se refiere a la propiedad idUser de la entidad Usuario
    List<UserComment> findByUserIdUserOrderByCreatedAtDesc(Integer userId);

    // Verificar si un usuario ya comentó en un negocio (OPCIONAL - puedes quitarlo si no lo usas)
    boolean existsByUserIdUserAndBusinessIdBusiness(Integer userId, Long businessId);

    // Obtener el promedio de rating de un negocio
    @Query("SELECT AVG(c.rating) FROM UserComment c WHERE c.business.idBusiness = :businessId")
    Double getAverageRatingByBusiness(@Param("businessId") Long businessId);

    // Contar comentarios por negocio
    long countByBusinessIdBusiness(Long businessId);
}