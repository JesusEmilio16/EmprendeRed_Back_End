package com.example.demo.controller.playground;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/ejemplo/canciones")
public class JdqpController {

    private List<String> canciones = new ArrayList<>(Arrays.asList("Bohemian Rhapsody", "Resolution", "Hotel California", "Unlasting"));

    // GET - Listar todas las canciones
    // Endpoint: GET /ejemplo/canciones
    @GetMapping
    public List<String> getCanciones() {
        return canciones;
    }

    // POST - Agregar nueva canción
    // Endpoint: POST /ejemplo/canciones?cancion=Nombre
    @PostMapping
    public String addCancion(@RequestParam String cancion) {
        canciones.add(cancion);
        return "Canción agregada: " + cancion;
    }

    // PUT - Actualizar una canción existente
    // Endpoint: PUT /ejemplo/canciones/{index}?nueva=Nombre
    @PutMapping("/{index}")
    public String updateCancion(@PathVariable int index, @RequestParam String nueva) {
        canciones.set(index, nueva);
        return "Canción actualizada en " + index + " -> " + nueva;
    }

    // DELETE - Eliminar una canción
    //Endpoint: DELETE /ejemplo/canciones/{index}
    @DeleteMapping("/{index}")
    public String deleteCancion(@PathVariable int index) {
        String eliminada = canciones.remove(index);
        return "Canción eliminada: " + eliminada;
    }
}

