package ejercicosClase.repository;

import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserclassRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
