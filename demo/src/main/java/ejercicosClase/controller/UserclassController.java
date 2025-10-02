package ejercicosClase.controller;

import com.example.demo.dto.UserClassResponse;
import com.example.demo.dto.UserclassRequest;
import com.example.demo.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userclass")
public class UserclassController {
    private final UsuarioService service;
    public UserclassController(UsuarioService service){
        this.service=service;
    }

    @PostMapping
    public UserClassResponse crear(@RequestBody UserclassRequest request){
        return service.crear(request);
    }

    @GetMapping
    public List<UserClassResponse> listar(){
        return service.listar();
    }

    @GetMapping("/{id}")
    public UserClassResponse buscarPorId(@PathVariable Long id){
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public UserClassResponse actualizar(@PathVariable Long id, @RequestBody UserclassRequest request){
        return service.actualizar(id,request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id){
        service.eliminar(id);
    }


}
