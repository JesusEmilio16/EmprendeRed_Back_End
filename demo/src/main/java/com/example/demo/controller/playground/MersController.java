
package com.example.demo.controller.playground;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("ejemplo/basket")
public class MersController {
    private List<String> basket = new ArrayList<>(List.of("Warriors","Lakers","BUCKS","Dallas"));

    //Get- Sirve para lsitar todos los equipos
    //Enpoint: get/ejemplo/basket
    @GetMapping
    public List<String> getBasket(){

        return basket;
    }

    //Post-Agregar nuevo equipo
    //Endpoint: Post /ejemplo/basket?equipo=celtics
    @PostMapping
    public String addBasket(@RequestParam String equipo){
        basket.add(equipo);
        System.out.println("Basket added");
        return "Equipo agregado"+equipo;
    }

    //PUT-Actualizar un equipo existente
    //Enpoint: Put /ejemplo/basket/3?nuevo=dallas mavericks
    @PutMapping("/{index}")
    public String updateBasket(@PathVariable int index, @RequestParam String nuevo){
        basket.set(index, nuevo);
        return "Equipo actualizado"+index+"es: "+nuevo;
    }

    //DELETE - Eliminar equipo
    //Enpoint: DELETE /ejemplo/basket/1
    @DeleteMapping("/{index}")
    public String deleteBasket(@PathVariable int index){
        String eliminado=basket.remove(index);
        return "Equipo eliminado"+index+"es: "+eliminado;
    }

}
