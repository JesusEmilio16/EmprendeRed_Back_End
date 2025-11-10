package com.example.demo.controller;

import com.example.demo.dto.BusinessRequest;
import com.example.demo.dto.BusinessResponse;
import com.example.demo.service.BusinessService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/business")
@CrossOrigin(origins = "http://localhost:4200")
public class BusinessController {

    private final BusinessService service;

    public BusinessController(BusinessService service) {
        this.service = service;
    }

    @GetMapping
    public List<BusinessResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public BusinessResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public BusinessResponse create(
            @RequestParam("nombreUsuario") String nombreUsuario,
            @RequestParam("name") String name,
            @RequestParam("direccion") String direccion,
            @RequestParam("barrio") String barrio,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        BusinessRequest req = new BusinessRequest();
        req.setNombreUsuario(nombreUsuario);
        req.setName(name);
        req.setDireccion(direccion);
        req.setBarrio(barrio);
        req.setDescription(description);
        req.setImage(image);

        return service.create(req);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public BusinessResponse update(
            @PathVariable Long id,
            @RequestParam("nombreUsuario") String nombreUsuario,
            @RequestParam("name") String name,
            @RequestParam("direccion") String direccion,
            @RequestParam("barrio") String barrio,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        BusinessRequest req = new BusinessRequest();
        req.setNombreUsuario(nombreUsuario);
        req.setName(name);
        req.setDireccion(direccion);
        req.setBarrio(barrio);
        req.setDescription(description);
        req.setImage(image);

        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
