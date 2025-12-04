package com.example.demo.controller;

import com.example.demo.dto.UserClassResponse;
import com.example.demo.dto.UserclassRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;
//@CrossOrigin(origins = "http://localhost:4200")
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
    public UserClassResponse getById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public UserClassResponse create(@RequestBody UserclassRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public UserClassResponse update(@PathVariable Integer id, @RequestBody UserclassRequest request) {
        return service.update(id,request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @GetMapping("/reporte/pdf")
    public ResponseEntity<byte[]> generarPdfUsuarios() {
        byte[] pdf = service.generarPdfUsuarios();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=usuarios.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportarExcel() {
        byte[] excel = service.exportarExcelUsuarios();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=usuarios.xls")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);
    }
}
