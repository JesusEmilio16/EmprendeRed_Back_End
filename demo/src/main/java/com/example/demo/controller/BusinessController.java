package com.example.demo.controller;

import com.example.demo.dto.BusinessRequest;
import com.example.demo.dto.BusinessResponse;
import com.example.demo.service.BusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/business")
/*@CrossOrigin(origins = "*")*/
public class BusinessController {

    private final BusinessService service;

    public BusinessController(BusinessService service) {
        this.service = service;
    }

    // 🟢 Crear negocio (solo usuario logueado)
    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<BusinessResponse> createBusiness(
            @RequestParam Long userId,
            @RequestParam String name,
            @RequestParam String direccion,
            @RequestParam String barrio,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile image
    ) {
        BusinessRequest request = new BusinessRequest();
        request.setName(name);
        request.setDireccion(direccion);
        request.setBarrio(barrio);
        request.setDescription(description);
        request.setImage(image);

        BusinessResponse response = service.create(userId, request);
        return ResponseEntity.ok(response);
    }

    // 🟡 Listar negocios del usuario logueado
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BusinessResponse>> getUserBusinesses(@PathVariable Long userId) {
        List<BusinessResponse> businesses = service.getByUser(userId);
        return ResponseEntity.ok(businesses);
    }

    // 🟣 Listar todos los negocios (público)
    @GetMapping("/all")
    public ResponseEntity<List<BusinessResponse>> getAllBusinesses() {
        List<BusinessResponse> businesses = service.getAll();
        return ResponseEntity.ok(businesses);
    }

    // 🟠 Actualizar negocio (solo si pertenece al usuario)
    @PutMapping(value = "/{id}/{userId}", consumes = {"multipart/form-data"})
    public ResponseEntity<BusinessResponse> updateBusiness(
            @PathVariable Long id,
            @PathVariable Long userId,
            @RequestParam String name,
            @RequestParam String direccion,
            @RequestParam String barrio,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile image
    ) {
        BusinessRequest request = new BusinessRequest();
        request.setName(name);
        request.setDireccion(direccion);
        request.setBarrio(barrio);
        request.setDescription(description);
        request.setImage(image);

        BusinessResponse response = service.update(id, userId, request);
        return ResponseEntity.ok(response);
    }

    // 🔴 Eliminar negocio (solo si pertenece al usuario)
    @DeleteMapping("/{id}/{userId}")
    public ResponseEntity<String> deleteBusiness(@PathVariable Long id, @PathVariable Long userId) {
        /*System.out.println( "este es el negocio controller a eliminar"+id);
        System.out.println( "este es el usuario  controller a eliminar"+userId);
        service.delete(id, userId);
        System.out.println( "este es el ususrio controller 3 a eliminar"+userId);
        return ResponseEntity.ok("Negocio eliminado correctamente");*/

        try {
            service.delete(id, userId);
            return ResponseEntity.ok("Negocio eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    // --- NUEVOS ENDPOINTS DE EXPORTACIÓN ---

    // 📥 Descargar Excel
    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportExcel() throws IOException {
        ByteArrayInputStream in = service.generateExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=emprendimientos.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    // 📥 Descargar PDF
    @GetMapping("/export/pdf")
    public ResponseEntity<InputStreamResource> exportPdf() {
        ByteArrayInputStream in = service.generatePdf();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=emprendimientos.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }
}
