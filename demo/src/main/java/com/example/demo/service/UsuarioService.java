package com.example.demo.service;

import com.example.demo.dto.UserclassRequest;
import com.example.demo.dto.UserClassResponse;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioRepository repo;
    public UsuarioService(UsuarioRepository repo) { this.repo = repo; }

    //traer usuarios
    public List<UserClassResponse> lista() {
        return repo.findAll()
                .stream()
                .map(u -> new UserClassResponse(u.getIdUser(),u.getName(),u.getMiddleName(),
                        u.getLastName(),u.getLastName2(),u.getEmail(),u.getPassword(),u.getPhoneNumber(),u.getDocumento(),
                        u.getSexo())
                ).collect(Collectors.toList());
    }

    //crear usuario
    public UserClassResponse create(UserclassRequest request) {
        Usuario entity = new Usuario();
        entity.setName(request.getName());
        entity.setMiddleName(request.getMiddleName());
        entity.setLastName(request.getLastName());
        entity.setLastName2(request.getLastName2());
        entity.setEmail(request.getEmail());
        entity.setPassword(request.getPassword());
        entity.setPhoneNumber(request.getPhoneNumber());
        entity.setDocumento(request.getDocumento());
        entity.setSexo(request.getSexo());

        Usuario saved = repo.save(entity);
        return new UserClassResponse(saved.getIdUser(),saved.getName(), saved.getMiddleName(),
                saved.getLastName(), saved.getLastName2(), saved.getEmail(), saved.getPassword(), saved.getPhoneNumber(),
                saved.getDocumento(), saved.getSexo());
    }

    //listar por id
    public UserClassResponse findById(Long idUser) {
        Usuario entity = repo.findById(idUser)
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"+ idUser) );
        return new UserClassResponse(entity.getIdUser(), entity.getName(), entity.getMiddleName(), entity.getLastName(),
                entity.getLastName2(), entity.getEmail(), entity.getPassword(), entity.getPhoneNumber(), entity.getDocumento(),
                entity.getSexo());
    }


    //actualizar usuarios
    public UserClassResponse update(Long idUser, UserclassRequest request) {
        Usuario entity = repo.findById(idUser)
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado con id"+ idUser) );
        entity.setName(request.getName());
        entity.setMiddleName(request.getMiddleName());
        entity.setLastName(request.getLastName());
        entity.setLastName2(request.getLastName2());
        entity.setEmail(request.getEmail());
        entity.setPassword(request.getPassword());
        //password: si viene en el body actualizarla (asegurar hashing en producción)
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            entity.setPassword(request.getPassword());
        }
        entity.setPhoneNumber(request.getPhoneNumber());
        entity.setDocumento(request.getDocumento());
        entity.setSexo(request.getSexo());

        Usuario saved = repo.save(entity);
        return new UserClassResponse(saved.getIdUser(), saved.getName(), saved.getMiddleName(),
                saved.getLastName(), saved.getLastName2(), saved.getEmail(), saved.getPassword(), saved.getPhoneNumber(),
                saved.getDocumento(), saved.getSexo()
        );
    }

    public void delete(Long id) {

        if (!repo.existsById(id)){
            throw new RuntimeException("Usuario no encontrado con id"+ id);
        }
        repo.deleteById(id);
    }
}
