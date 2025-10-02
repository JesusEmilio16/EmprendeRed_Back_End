package ejercicosClase.service;

import com.example.demo.dto.UserClassResponse;
import com.example.demo.dto.UserclassRequest;
import com.example.demo.model.Userclass;
import com.example.demo.repository.UserclassRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserclassService {

    private final UserclassRepository repository;

    public UserclassService(UserclassRepository repository) {
        this.repository = repository;
    }

    //crear un nuevo usuario
    public  UserClassResponse crear(UserclassRequest request){
        Userclass entity = new Userclass();
        entity.setNombre(request.getNombre());
        entity.setEmail(request.getEmail());
        entity.setTelefono(request.getTelefono());

        Userclass guardado = repository.save(entity);
        return new UserClassResponse(guardado.getId(),guardado.getNombre(),guardado.getEmail(),guardado.getTelefono());
    }

    //listar todos los uduarios
    public List<UserClassResponse> listar(){
        return repository.findAll()
                .stream()
                .map( u -> new UserClassResponse(u.getId(),u.getNombre(),u.getEmail(),u.getTelefono()))
                .collect(Collectors.toList());
    }

    //listar por id
    public UserClassResponse buscarPorId(Long id){
        Userclass entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException( "usuario no encontrado " + id));
        return new UserClassResponse(entity.getId(),entity.getNombre(),entity.getEmail(),entity.getTelefono());
    }

    //actualizar usuario por id
    public UserClassResponse actualizar(Long id, UserclassRequest request){
        Userclass entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException( "usuario no encontrado con id " + id));
        entity.setNombre(request.getNombre());
        entity.setEmail(request.getEmail());
        entity.setTelefono(request.getTelefono());

        Userclass actualizado = repository.save(entity);
        return new UserClassResponse(actualizado.getId(),actualizado.getNombre(),actualizado.getEmail(),actualizado.getTelefono());
    }

    //eliminar
    public void eliminar(Long id){
        if(!repository.existsById(id)){
            throw new RuntimeException( "usuario no encontrado con id " + id);
        }
        repository.deleteById(id);
    }
}
