package com.example.demo.service;

import com.example.demo.dto.UserCommentResponse;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CommentsReportService {

    @Autowired
    private UserCommentService commentService;

    public ByteArrayOutputStream generateExcelReport(Long businessId) {
        List<UserCommentResponse> comments =
                (businessId != null && businessId > 0)
                        ? commentService.getCommentsByBusiness(businessId)
                        : commentService.getAllComments();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Comentarios");

            // ===== Estilos =====
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);

            // ===== Título =====
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("REPORTE DE COMENTARIOS");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));

            // ===== Fecha =====
            Row dateRow = sheet.createRow(1);
            dateRow.createCell(0)
                    .setCellValue("Fecha de generación: " +
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 0, 5));

            sheet.createRow(2); // espacio

            // ===== Encabezados =====
            String[] columns = {"ID", "Usuario", "Negocio", "Comentario", "Rating", "Fecha"};
            Row headerRow = sheet.createRow(3);

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // ===== Datos =====
            int rowNum = 4;

            for (UserCommentResponse comment : comments) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(comment.getId());
                row.createCell(1).setCellValue(comment.getUserName() + " " + comment.getUserLastName());
                row.createCell(2).setCellValue(comment.getBusinessName());
                row.createCell(3).setCellValue(comment.getContent());
                row.createCell(4).setCellValue(comment.getRating().doubleValue());
                
                row.createCell(5).setCellValue(
                        comment.getCreatedAt()
                                .toLocalDateTime()
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                );
            }


            // Auto-size
            for (int i = 0; i < columns.length; i++) sheet.autoSizeColumn(i);

            workbook.write(outputStream);
            return outputStream;

        } catch (Exception e) {
            throw new RuntimeException("Error al generar Excel: " + e.getMessage(), e);
        }
    }


    public ByteArrayOutputStream generatePdfReport(Long businessId) {
        List<UserCommentResponse> comments =
                (businessId != null && businessId > 0)
                        ? commentService.getCommentsByBusiness(businessId)
                        : commentService.getAllComments();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // ===== Fuentes =====
            com.lowagie.text.Font titleFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 18, com.lowagie.text.Font.BOLD, Color.BLACK);
            com.lowagie.text.Font dateFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.NORMAL, Color.GRAY);
            com.lowagie.text.Font headerFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12, com.lowagie.text.Font.BOLD, Color.WHITE);
            com.lowagie.text.Font cellFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.NORMAL, Color.BLACK);

            // ===== Título =====
            Paragraph title = new Paragraph("REPORTE DE COMENTARIOS", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // ===== Fecha =====
            Paragraph date = new Paragraph(
                    "Fecha de generación: " +
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                    dateFont
            );
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(20);
            document.add(date);

            // ===== Tabla =====
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            float[] widths = {1f, 2f, 2f, 4f, 1.5f, 2f};
            table.setWidths(widths);

            // ===== Encabezados =====
            String[] headers = {"ID", "Usuario", "Negocio", "Comentario", "Rating", "Fecha"};

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(new Color(25, 25, 112));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(6);
                table.addCell(cell);
            }

            // ===== Datos =====
            for (UserCommentResponse c : comments) {
                table.addCell(new Phrase(String.valueOf(c.getId()), cellFont));
                table.addCell(new Phrase(c.getUserName() + " " + c.getUserLastName(), cellFont));
                table.addCell(new Phrase(c.getBusinessName(), cellFont));
                table.addCell(new Phrase(c.getContent(), cellFont));
                table.addCell(new Phrase(String.valueOf(c.getRating()), cellFont));
                table.addCell(new Phrase(
                        c.getCreatedAt()
                                .toLocalDateTime()
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                        cellFont
                ));
            }

            document.add(table);

            // ===== Footer =====
            Paragraph footer = new Paragraph(
                    "Total de comentarios: " + comments.size(),
                    new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10, com.lowagie.text.Font.ITALIC, Color.GRAY)
            );
            footer.setAlignment(Element.ALIGN_RIGHT);
            footer.setSpacingBefore(10);
            document.add(footer);

            document.close();
            return outputStream;

        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF: " + e.getMessage(), e);
        }
    }
}