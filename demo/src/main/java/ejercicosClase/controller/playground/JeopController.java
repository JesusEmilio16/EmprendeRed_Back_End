
package ejercicosClase.controller.playground;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("ejemplo/nombres")
public class JeopController {
    private List<String> nombres = new ArrayList<>(List.of("Maicol","Jesus","Yademir","Wilder"));

    //Get- Sirve para listar todos los nombre
    //Enpoint: get/ejemplo/nombres
    @GetMapping
    public List<String> getNombres(){

        return nombres;
    }

    // GET- sirve para hacer get con una variable
    //Enpoint: get/ejemplo/nombres/1
    @GetMapping("/{index}")
    public String getNombres(@PathVariable int index){
        String datoFinal = nombres.get(index);
        return "dato traido "+ datoFinal;
    }

    //Post-Agregar nuevo nombre
    //Endpoint: Post /ejemplo/nombres?nombre=jhonatan
    @PostMapping
    public String addNombres(@RequestParam String nombre){
        nombres.add(nombre);
        System.out.println("nombres added");
        return "nombre agregado"+nombre;
    }

    //PUT-Actualizar un nombre existente
    //Enpoint: Put /ejemplo/nombres/3?nuevo= nombre nuevo
    @PutMapping("/{index}")
    public String updateNombres(@PathVariable int index, @RequestParam String nuevo){
        nombres.set(index, nuevo);
        return "nombre actualizado "+index+" es: "+nuevo;
    }

    //DELETE - Eliminar nombre
    //Enpoint: DELETE /ejemplo/nombres/1
    @DeleteMapping("/{index}")
    public String deleteNombres(@PathVariable int index){
        String eliminado=nombres.remove(index);
        return "nombre eliminado "+index+" es: "+eliminado;
    }

}
