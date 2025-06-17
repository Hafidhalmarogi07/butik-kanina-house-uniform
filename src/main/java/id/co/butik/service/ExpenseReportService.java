package id.co.butik.service;

import id.co.butik.entity.Expense;
import id.co.butik.repository.ExpenseReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class ExpenseReportService {

    @Autowired
    private ExpenseReportRepository expenseReportRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Generate expense report in Excel format
     * @param startDate the start date
     * @param endDate the end date
     * @return ByteArrayInputStream containing the Excel file
     */
    public ByteArrayInputStream generateExcelReport(LocalDate startDate, LocalDate endDate) throws IOException {
        List<Expense> expenses = expenseReportRepository.findExpensesByDateRange(startDate, endDate);

        try (Workbook workbook = new XSSFWorkbook(); 
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Expense Report");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Date");
            headerRow.createCell(1).setCellValue("Description");
            headerRow.createCell(2).setCellValue("Amount");
            headerRow.createCell(3).setCellValue("Type");
            headerRow.createCell(4).setCellValue("Payment Method");
            headerRow.createCell(5).setCellValue("Note");
            headerRow.createCell(6).setCellValue("Created By");

            // Create data rows
            int rowIdx = 1;
            for (Expense expense : expenses) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(expense.getDate().toString());
                row.createCell(1).setCellValue(expense.getDescription());
                row.createCell(2).setCellValue(expense.getAmount().doubleValue());
                row.createCell(3).setCellValue(expense.getType() != null ? expense.getType().toString() : "N/A");
                row.createCell(4).setCellValue(expense.getPaymentMethod() != null ? expense.getPaymentMethod().toString() : "N/A");
                row.createCell(5).setCellValue(expense.getNote() != null ? expense.getNote() : "");
                row.createCell(6).setCellValue(expense.getCreatedBy() != null ? expense.getCreatedBy().getFullName() : "N/A");
            }

            // Resize columns to fit content
            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    /**
     * Generate expense report in PDF format
     * @param startDate the start date
     * @param endDate the end date
     * @return ByteArrayInputStream containing the PDF file
     */
    public ByteArrayInputStream generatePdfReport(LocalDate startDate, LocalDate endDate) throws DocumentException {
        List<Expense> expenses = expenseReportRepository.findExpensesByDateRange(startDate, endDate);

        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add title
            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Expense Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Add date range
            com.itextpdf.text.Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph dateRange = new Paragraph(
                    "Period: " + startDate.format(DATE_FORMATTER) + " to " + endDate.format(DATE_FORMATTER),
                    dateFont);
            dateRange.setAlignment(Element.ALIGN_CENTER);
            document.add(dateRange);

            document.add(Chunk.NEWLINE);

            // Create table
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);

            // Add table headers
            com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Stream.of("Date", "Description", "Amount", "Type", "Payment Method", "Note", "Created By")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle, headerFont));
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(header);
                    });

            // Add data rows
            for (Expense expense : expenses) {
                table.addCell(expense.getDate().toString());
                table.addCell(expense.getDescription());
                table.addCell(expense.getAmount().toString());
                table.addCell(expense.getType() != null ? expense.getType().toString() : "N/A");
                table.addCell(expense.getPaymentMethod() != null ? expense.getPaymentMethod().toString() : "N/A");
                table.addCell(expense.getNote() != null ? expense.getNote() : "");
                table.addCell(expense.getCreatedBy() != null ? expense.getCreatedBy().getFullName() : "N/A");
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            log.error("Error generating PDF report", e);
            throw e;
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
    
    /**
     * Convert LocalDateTime to LocalDate for the controller
     * @param dateTime the LocalDateTime to convert
     * @return LocalDate
     */
    public LocalDate convertToLocalDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate() : null;
    }
}