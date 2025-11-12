package com.example.demo.repository;

import com.example.demo.model.Business;
import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    List<Business> findByUsuario(Usuario usuario);
    List<Business> findByUsuario_IdUser(Long idUser); // ✅ Este método lo usa el servicio
}

