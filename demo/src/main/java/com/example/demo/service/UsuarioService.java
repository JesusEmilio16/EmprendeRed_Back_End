package com.example.demo.service;

import com.example.demo.dto.UserclassRequest;
import com.example.demo.dto.UserClassResponse;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
//login
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
//pdf
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;


import org.apache.poi.hssf.usermodel.HSSFWorkbook; // Para .xls
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;

// 1. IMPORTS DE PDF (OpenPDF) - Quitamos el ByteArrayOutputStream duplicado de aquí si estaba.
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

// 2. IMPORTS DE EXCEL (Apache POI) - Específicos (Incluyendo XSSF)
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // <-- ¡CLAVE!

// util IO y colecciones
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream; // <-- ¡NUEVO! Para que el service devuelva lo que tu compañero devuelve
import java.util.List;
import java.util.stream.Collectors;
import java.util.Date; // Asegúrate de tener esta importación
import com.lowagie.text.Phrase;


// 2. IMPORTS DE EXCEL (Apache POI) - Específicos
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

// util IO y colecciones

import java.util.List;

import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final UsuarioRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Constructor: inyecta repo + passwordEncoder
    public UsuarioService(UsuarioRepository repo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // traer usuarios (no devolvemos password)
    public List<UserClassResponse> lista() {
        return repo.findAll()
                .stream()
                .map(u -> new UserClassResponse(
                        u.getIdUser(),
                        u.getName(),
                        u.getMiddleName(),
                        u.getLastName(),
                        u.getLastName2(),
                        u.getEmail(),
                        null, // NO devolver password
                        u.getPhoneNumber(),
                        u.getDocumento(),
                        u.getSexo()
                ))
                .collect(Collectors.toList());
    }

    // crear usuario (ENCRIPTA contraseña)
    public UserClassResponse create(UserclassRequest request) {
        Usuario entity = new Usuario();
        entity.setName(request.getName());
        entity.setMiddleName(request.getMiddleName());
        entity.setLastName(request.getLastName());
        entity.setLastName2(request.getLastName2());
        entity.setEmail(request.getEmail());
        // ENCRIPTAR la contraseña antes de guardar
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setPhoneNumber(request.getPhoneNumber());
        entity.setDocumento(request.getDocumento());
        entity.setSexo(request.getSexo());


        Usuario saved = repo.save(entity);
        return new UserClassResponse(
                saved.getIdUser(),
                saved.getName(),
                saved.getMiddleName(),
                saved.getLastName(),
                saved.getLastName2(),
                saved.getEmail(),
                null, // NO devolver password
                saved.getPhoneNumber(),
                saved.getDocumento(),
                saved.getSexo()
        );
    }

    // listar por id (no devolvemos password)
    public UserClassResponse findById(Integer idUser) {
        Usuario entity = repo.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado " + idUser));
        return new UserClassResponse(
                entity.getIdUser(),
                entity.getName(),
                entity.getMiddleName(),
                entity.getLastName(),
                entity.getLastName2(),
                entity.getEmail(),
                null, // NO devolver password
                entity.getPhoneNumber(),
                entity.getDocumento(),
                entity.getSexo()
        );
    }

    // actualizar usuario (si viene password la encriptamos)
    public UserClassResponse update(Integer idUser, UserclassRequest request) {
        Usuario entity = repo.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + idUser));
        entity.setName(request.getName());
        entity.setMiddleName(request.getMiddleName());
        entity.setLastName(request.getLastName());
        entity.setLastName2(request.getLastName2());
        entity.setEmail(request.getEmail());

        // Si llega password en el body, la encriptamos y actualizamos
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            entity.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        entity.setPhoneNumber(request.getPhoneNumber());
        entity.setDocumento(request.getDocumento());
        entity.setSexo(request.getSexo());

        Usuario saved = repo.save(entity);
        return new UserClassResponse(
                saved.getIdUser(),
                saved.getName(),
                saved.getMiddleName(),
                saved.getLastName(),
                saved.getLastName2(),
                saved.getEmail(),
                null, // NO devolver password
                saved.getPhoneNumber(),
                saved.getDocumento(),
                saved.getSexo()
        );
    }

    public void delete(Integer id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id " + id);
        }
        repo.deleteById(id);
    }

    public LoginResponse login(LoginRequest request) {
        // Buscar usuario por email
        Usuario user = repo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        // Comparar contraseña (plaintext vs hashed)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        System.out.println("ID: " + user.getIdUser());
        // Generar token: subject = idUser (string) o email, según prefieras
        String token = jwtUtil.generateToken(String.valueOf(user.getIdUser()));


        // Devolver DTO con token
        return new LoginResponse(
                user.getIdUser(),
                user.getName(),
                user.getEmail(),
                token
        );
    }

    public byte[] generarPdfUsuarios() {
        try {
            List<Usuario> usuarios = repo.findAll();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, out);

            document.open();

            com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("REPORTE DE USUARIOS", (com.lowagie.text.Font) titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("Generado automáticamente"));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);

            table.addCell("ID");
            table.addCell("Nombre");
            table.addCell("Segundo Nombre");
            table.addCell("Apellido");
            table.addCell("Apellido 2");
            table.addCell("Email");
            table.addCell("Teléfono");
            table.addCell("Documento");
            table.addCell("Sexo");

            for (Usuario u : usuarios) {
                table.addCell(String.valueOf(u.getIdUser()));
                table.addCell(u.getName());
                table.addCell(u.getMiddleName() != null ? u.getMiddleName() : "");
                table.addCell(u.getLastName());
                table.addCell(u.getLastName2() != null ? u.getLastName2() : "");
                table.addCell(u.getEmail());
                table.addCell(u.getPhoneNumber());
                table.addCell(u.getDocumento());
                table.addCell(u.getSexo() != null ? u.getSexo() : "");
            }

            document.add(table);
            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }

    public ByteArrayInputStream generarExcelUsuarios() {
        try {
            // 1. Obtener la lista de usuarios
            List<Usuario> usuarios = repo.findAll();

            // 2. CAMBIO: Crear un nuevo libro de trabajo de Excel (.xlsx)
            // Usamos try-with-resources para cerrar el workbook automáticamente
            try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                Sheet sheet = workbook.createSheet("Usuarios");

                // --- Estilos ---
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 14);
                headerStyle.setFont(headerFont);

                CellStyle columnHeaderStyle = workbook.createCellStyle();
                Font columnHeaderFont = workbook.createFont();
                columnHeaderFont.setBold(true);
                columnHeaderStyle.setFont(columnHeaderFont);

                // 3. Crear el encabezado del reporte (Idéntico a tu implementación original)
                Row titleRow = sheet.createRow(0);
                Cell titleCell = titleRow.createCell(0);
                titleCell.setCellValue("REPORTE DE USUARIOS - EMPRENDERED");
                titleCell.setCellStyle(headerStyle);

                Row dateRow = sheet.createRow(1);
                Cell dateCell = dateRow.createCell(0);
                dateCell.setCellValue("Fecha de Generación: " + new java.util.Date());

                Row row = sheet.createRow(3);
                String[] headers = {"ID", "Nombre", "Segundo Nombre", "Apellido", "Apellido 2", "Email", "Teléfono", "Documento", "Sexo"};

                for (int i = 0; i < headers.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(columnHeaderStyle);
                }

                // 4. Llenar la tabla con los datos (Idéntico a tu implementación original)
                int rowNum = 4;
                for (Usuario u : usuarios) {
                    Row dataRow = sheet.createRow(rowNum++);

                    int colNum = 0;
                    dataRow.createCell(colNum++).setCellValue(u.getIdUser());
                    dataRow.createCell(colNum++).setCellValue(u.getName());
                    dataRow.createCell(colNum++).setCellValue(u.getMiddleName() != null ? u.getMiddleName() : "");
                    dataRow.createCell(colNum++).setCellValue(u.getLastName());
                    dataRow.createCell(colNum++).setCellValue(u.getLastName2() != null ? u.getLastName2() : "");
                    dataRow.createCell(colNum++).setCellValue(u.getEmail());
                    dataRow.createCell(colNum++).setCellValue(u.getPhoneNumber());
                    dataRow.createCell(colNum++).setCellValue(u.getDocumento());
                    dataRow.createCell(colNum++).setCellValue(u.getSexo() != null ? u.getSexo() : "");
                }

                // 5. Autoajustar
                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                // 6. Escribir el libro de trabajo en el stream y devolver ByteArrayInputStream
                workbook.write(out);
                return new ByteArrayInputStream(out.toByteArray());
            } // El try-with-resources cierra el workbook y el ByteArrayOutputStream

        } catch (Exception e) {
            throw new RuntimeException("Error generando el archivo Excel (.xlsx)", e);
        }
    }
}