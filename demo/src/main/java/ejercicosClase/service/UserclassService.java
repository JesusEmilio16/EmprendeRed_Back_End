/*
package ejercicosClase.service;

import com.example.demo.dto.UserClassResponse;
import com.example.demo.dto.UserclassRequest;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserclassService {

    private final UsuarioRepository repository;

    public UserclassService(UsuarioRepository repository) {
        this.repository = repository;
    }

    //crear un nuevo usuario
    public  UserClassResponse crear(UserclassRequest request){
        Usuario entity = new Usuario();
        entity.setNombre(request.getNombre());
        entity.setEmail(request.getEmail());
        entity.setTelefono(request.getTelefono());

        Usuario guardado = repository.save(entity);
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
        Usuario entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException( "usuario no encontrado " + id));
        return new UserClassResponse(entity.getId(),entity.getNombre(),entity.getEmail(),entity.getTelefono());
    }

    //actualizar usuario por id
    public UserClassResponse actualizar(Long id, UserclassRequest request){
        Usuario entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException( "usuario no encontrado con id " + id));
        entity.setNombre(request.getNombre());
        entity.setEmail(request.getEmail());
        entity.setTelefono(request.getTelefono());

        Usuario actualizado = repository.save(entity);
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
*/
