package com.example.demo.controller;

import com.example.demo.dto.BusinessRequest;
import com.example.demo.dto.BusinessResponse;
import com.example.demo.service.BusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Hidden;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/business")
@Tag(name = "Gestión de Negocios", description = "Endpoints para crear, editar, listar y exportar emprendimientos")
public class BusinessController {


    private final BusinessService service;

    public BusinessController(BusinessService service) {
        this.service = service;
    }

    // 🟢 CREAR
    @Operation(summary = "Crear nuevo emprendimiento", description = "Registra un negocio con imagen. Requiere Multipart/Form-Data.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Creado exitosamente", content = @Content(schema = @Schema(implementation = BusinessResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BusinessResponse> createBusiness(
            @Parameter(description = "ID del usuario dueño") @RequestParam Long userId,
            @Parameter(description = "Nombre del negocio") @RequestParam String name,
            @Parameter(description = "Dirección física") @RequestParam String direccion,
            @Parameter(description = "Barrio") @RequestParam String barrio,
            @Parameter(description = "Descripción del servicio") @RequestParam String description,
            @Parameter(description = "Imagen (opcional)") @RequestParam(required = false) MultipartFile image
    ) {
        BusinessRequest request = new BusinessRequest();
        request.setName(name); request.setDireccion(direccion); request.setBarrio(barrio); request.setDescription(description); request.setImage(image);
        return ResponseEntity.ok(service.create(userId, request));
    }

    // 🟡 LISTAR POR USUARIO
    @Operation(summary = "Listar mis negocios", description = "Obtiene los negocios de un usuario específico.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BusinessResponse>> getUserBusinesses(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getByUser(userId));
    }

    // 🟣 LISTAR TODOS
    @Operation(summary = "Listar todo (Público)", description = "Devuelve todos los negocios registrados en el sistema.")
    @GetMapping("/all")
    public ResponseEntity<List<BusinessResponse>> getAllBusinesses() {
        return ResponseEntity.ok(service.getAll());
    }

    // 🔵 OBTENER POR ID
    @Operation(summary = "Obtener detalle", description = "Busca un negocio específico por su ID.")
    @GetMapping("/{id}")
    public ResponseEntity<BusinessResponse> getBusinessById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // 🟠 ACTUALIZAR
    @Operation(summary = "Actualizar negocio", description = "Modifica datos de un negocio existente. Solo el dueño puede hacerlo.")
    @PutMapping(value = "/{id}/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
        request.setName(name); request.setDireccion(direccion); request.setBarrio(barrio); request.setDescription(description); request.setImage(image);
        return ResponseEntity.ok(service.update(id, userId, request));
    }

    // 🔴 ELIMINAR
    @Operation(summary = "Eliminar negocio", description = "Borra el registro de la base de datos.")
    @DeleteMapping("/{id}/{userId}")
    public ResponseEntity<String> deleteBusiness(@PathVariable Long id, @PathVariable Long userId) {
        try { service.delete(id, userId); return ResponseEntity.ok("Eliminado"); }
        catch (RuntimeException e) { return ResponseEntity.status(403).body(e.getMessage()); }
    }

    // 📥 EXPORTAR EXCEL
    @Operation(summary = "Descargar reporte Excel", description = "Genera un archivo .xlsx con todos los negocios.")
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

    // 📥 EXPORTAR PDF
    @Operation(summary = "Descargar reporte PDF", description = "Genera un archivo .pdf con la lista de negocios.")
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