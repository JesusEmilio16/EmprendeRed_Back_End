package com.example.demo.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/varios")
public class VariosController {
    @GetMapping("/aleatorios")
    public int aleatorios() {
        return (int) (Math.random() * 100+1);
    }

    @GetMapping("/nombreedad")
    public String nombreedad(@RequestParam int edad, @RequestParam String nombre ) {
        return "Nombre: " + nombre + " Edad: " + edad;
    }

    @GetMapping("/estudiante")
    public List<String> estudiante() {
        return List.of("Marco", "Marco", "Marco","mano","manito");
    }

    private String[] emoji={
            "😥","😁","😎"
    };

    @GetMapping("/emoji")
    public String emoji() {
        Random random = new Random();
        int index = random.nextInt(emoji.length);
        return emoji[index];
    }
}
