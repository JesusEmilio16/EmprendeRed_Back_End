package com.example.demo.controller;

import com.example.demo.dto.UserClassResponse;
import com.example.demo.dto.UserclassRequest;
import com.example.demo.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios2")

public class UsuarioController {
    private final UsuarioService service;
    public UsuarioController(UsuarioService service) { this.service = service; }

    @GetMapping
    public List<UserClassResponse> lista() {
        return service.lista();
    }

    @GetMapping("/{id}")
    public UserClassResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public UserClassResponse create(@RequestBody UserclassRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public UserClassResponse update(@PathVariable Long id, @RequestBody UserclassRequest request) {
        return service.update(id,request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
