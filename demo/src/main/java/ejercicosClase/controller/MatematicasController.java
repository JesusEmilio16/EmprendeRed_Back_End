package ejercicosClase.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatematicasController {
    @GetMapping("/suma")
    public int sumar(@RequestParam int a, @RequestParam int b) {
        return a + b;
    }

    @GetMapping("/resta")
    public int restar(@RequestParam int a, @RequestParam int b) {
        return a - b;
    }
    @GetMapping("/multi")
    public int multi(@RequestParam int a, @RequestParam int b) {
        return a * b;
    }
    @GetMapping("/cuadrado")
    public int cuadrado(@RequestParam int a) {
        return a * a;
    }

}
