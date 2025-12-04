package com.example.demo.service;

import com.example.demo.dto.BusinessRequest;
import com.example.demo.dto.BusinessResponse;
import com.example.demo.model.Business;
import com.example.demo.model.Usuario;
import com.example.demo.repository.BusinessRepository;
import com.example.demo.repository.UsuarioRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.*;
import java.io.IOException;
import java.nio.file.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

// 1. IMPORTS DE PDF (OpenPDF) - Específicos para evitar conflicto con Cell/Row de Excel
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;


// 2. IMPORTS DE EXCEL (Apache POI) - Específicos
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


import java.nio.file.*;


@Service
public class BusinessService {

    private final BusinessRepository businessRepo;
    private final UsuarioRepository usuarioRepo;

    // Ruta donde se guardarán las imágenes (relativa al proyecto)
    @Value("${upload.path:uploads/}")
    private String uploadPath;

    public BusinessService(BusinessRepository businessRepo, UsuarioRepository usuarioRepo) {
        this.businessRepo = businessRepo;
        this.usuarioRepo = usuarioRepo;
    }

    // --- NUEVOS MÉTODOS PARA EXPORTAR ---

    // 1. Generar Excel
    public ByteArrayInputStream generateExcel() throws IOException {
        List<Business> businesses = businessRepo.findAll();
        String[] columns = {"ID", "Nombre", "Dirección", "Barrio", "Descripción", "Fecha Creación"};

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Negocios");

            // Estilo para encabezado
            CellStyle headerCellStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerCellStyle.setFont(headerFont);

            // Crear fila encabezado
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // Llenar datos
            int rowIdx = 1;
            for (Business business : businesses) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(business.getIdBusiness());
                row.createCell(1).setCellValue(business.getName());
                row.createCell(2).setCellValue(business.getDireccion());
                row.createCell(3).setCellValue(business.getBarrio());
                row.createCell(4).setCellValue(business.getDescription());
                row.createCell(5).setCellValue(business.getCreatedAt().toString());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    // 2. Generar PDF
    public ByteArrayInputStream generatePdf() {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Título
            com.lowagie.text.Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLUE);
            Paragraph title = new Paragraph("Reporte de Emprendimientos", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Fecha
            Paragraph date = new Paragraph("Fecha de generación: " + new java.util.Date());
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(20);
            document.add(date);

            // Tabla
            PdfPTable table = new PdfPTable(5); // 5 columnas
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 3, 3, 2, 4});

            // Encabezados
            String[] headers = {"ID", "Nombre", "Dirección", "Barrio", "Descripción"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header));
                cell.setBackgroundColor(Color.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            // Datos
            List<Business> businesses = businessRepo.findAll();
            for (Business b : businesses) {
                table.addCell(String.valueOf(b.getIdBusiness()));
                table.addCell(b.getName());
                table.addCell(b.getDireccion());
                table.addCell(b.getBarrio());
                table.addCell(b.getDescription());
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // 🟢 Crear negocio asociado al usuario logueado
    public BusinessResponse create(Long userId, BusinessRequest request) {
        Usuario usuario = usuarioRepo.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + userId));

        Business entity = new Business();
        entity.setUsuario(usuario);
        entity.setName(request.getName());
        entity.setDireccion(request.getDireccion());
        entity.setBarrio(request.getBarrio());
        entity.setDescription(request.getDescription());
        entity.setIsActive(true);
        entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        // Guardar imagen si existe
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            entity.setImagePath(saveImage(request.getImage()));
        }

        Business saved = businessRepo.save(entity);
        return toResponse(saved);
    }

    // 🟡 Obtener negocios del usuario logueado
    public List<BusinessResponse> getByUser(Long userId) {
        return businessRepo.findByUsuario_IdUser(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 🟣 Obtener todos los negocios (público)
    public List<BusinessResponse> getAll() {
        return businessRepo.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 🟠 Actualizar negocio (solo si pertenece al usuario)
    public BusinessResponse update(Long id, Long userId, BusinessRequest request) {
        Business entity = businessRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado con id: " + id));

        if (!entity.getUsuario().getIdUser().equals(userId)) {
            throw new RuntimeException("No tienes permiso para modificar este negocio.");
        }

        entity.setName(request.getName());
        entity.setDireccion(request.getDireccion());
        entity.setBarrio(request.getBarrio());
        entity.setDescription(request.getDescription());

        // Reemplazar imagen si se sube una nueva
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            entity.setImagePath(saveImage(request.getImage()));
        }

        Business updated = businessRepo.save(entity);
        return toResponse(updated);
    }

    // 🔴 Eliminar negocio (solo si pertenece al usuario)
    public void delete(Long id, Long userId) {
        System.out.println( "este es el usuario service a eliminar"+userId);
        System.out.println( "este es el negocio service a eliminar"+id);
        Business entity = businessRepo.findById(id)

                .orElseThrow(() -> new RuntimeException("Negocio no encontrado con id: " + id));

        // Validación segura
        if (entity.getUsuario() == null) {
            throw new RuntimeException("El negocio no tiene usuario asociado.");
        }

        Long ownerId = entity.getUsuario().getIdUser().longValue();


        if (!ownerId.equals(userId)) {
            System.out.println( "este es el negocio service 2 a eliminar"+ownerId);
            System.out.println( "este es el ususario service 2 a eliminar"+userId);
            throw new RuntimeException("No tienes permiso para eliminar este negocio bl bla.");
        }

        businessRepo.delete(entity);
    }

    // 💾 Guardar imagen en carpeta "uploads" y devolver URL accesible
    private String saveImage(MultipartFile file) {
        if (file.isEmpty()) return null;

        try {
            Path uploadDir = Paths.get(uploadPath);

            // Crear carpeta si no existe
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Nombre único para la imagen
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);

            // Guardar archivo
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Devuelve URL accesible públicamente (relativa al backend)
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar imagen: " + e.getMessage());
        }
    }

    // 🧱 Convertir entidad a DTO
    private BusinessResponse toResponse(Business entity) {
        return new BusinessResponse(
                entity.getIdBusiness(),
                entity.getUsuario().getName(),
                entity.getName(),
                entity.getDireccion(),
                entity.getBarrio(),
                entity.getDescription(),
                entity.getImagePath(),
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null,
                entity.getIsActive()
        );
    }
}
