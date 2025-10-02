package ejercicosClase.repository;

import com.example.demo.model.Userclass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserclassRepository extends JpaRepository<Userclass, Long> {
    Optional<Userclass> findByEmail(String email);
}
