package id.co.butik.service;

import id.co.butik.entity.Sale;
import id.co.butik.entity.SaleDetail;
import id.co.butik.repository.SalesReportRepository;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Stream;

@Service
@Slf4j
public class SalesReportService {

    @Autowired
    private SalesReportRepository salesReportRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Generate sales report in Excel format
     * @param startDate the start date
     * @param endDate the end date
     * @return ByteArrayInputStream containing the Excel file
     */
    public ByteArrayInputStream generateExcelReport(String options, LocalDate startDate, LocalDate endDate) throws IOException {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        LocalDate today = LocalDate.now();

        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("options is empty");
        }
        switch (options.toLowerCase()) {
            case "daily":
                startDateTime = today.atStartOfDay();
                endDateTime = today.atTime(23, 59, 59);
                break;
            case "weekly":
                // Mulai dari hari Senin minggu ini sampai Minggu
                LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
                LocalDate endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);
                startDateTime = startOfWeek.atStartOfDay();
                endDateTime = endOfWeek.atTime(23, 59, 59);
                break;
            case "monthly":
                LocalDate firstDayOfMonth = today.withDayOfMonth(1);
                LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
                startDateTime = firstDayOfMonth.atStartOfDay();
                endDateTime = lastDayOfMonth.atTime(23, 59, 59);
                break;
            case "quarterly":
                int currentMonth = today.getMonthValue();
                int startMonth = ((currentMonth - 1) / 3) * 3 + 1;
                LocalDate startOfQuarter = LocalDate.of(today.getYear(), startMonth, 1);
                LocalDate endOfQuarter = startOfQuarter.plusMonths(2).withDayOfMonth(startOfQuarter.plusMonths(2).lengthOfMonth());
                startDateTime = startOfQuarter.atStartOfDay();
                endDateTime = endOfQuarter.atTime(23, 59, 59);
                break;
            case "yearly":
                LocalDate firstDayOfYear = today.withDayOfYear(1);
                LocalDate lastDayOfYear = today.withDayOfYear(today.lengthOfYear());
                startDateTime = firstDayOfYear.atStartOfDay();
                endDateTime = lastDayOfYear.atTime(23, 59, 59);
                break;
            case "custom":
                if (startDate == null || endDate == null) {
                    throw new IllegalArgumentException("startDate and endDate must not be null for custom option");
                }
                startDateTime = startDate.atStartOfDay();
                endDateTime = endDate.atTime(23, 59, 59);
                break;
            default:
                throw new IllegalArgumentException("Invalid report option: " + options);
        }

        List<Sale> sales = salesReportRepository.findSalesByDateRange(startDateTime, endDateTime);

        try (Workbook workbook = new XSSFWorkbook(); 
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Sales Report");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Invoice Number");
            headerRow.createCell(1).setCellValue("Date");
            headerRow.createCell(2).setCellValue("Customer");
            headerRow.createCell(3).setCellValue("Admin");
            headerRow.createCell(4).setCellValue("Total Items");
            headerRow.createCell(5).setCellValue("Total Amount");
            headerRow.createCell(6).setCellValue("Status");

            // Create data rows
            int rowIdx = 1;
            int totalItems = 0;
            double totalAmount = 0.0;
            for (Sale sale : sales) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(sale.getInvoiceNumber());
                row.createCell(1).setCellValue(sale.getDate().format(DATE_FORMATTER));
                row.createCell(2).setCellValue(sale.getCustomer() != null ? sale.getCustomer().getNama() : "N/A");
                row.createCell(3).setCellValue(sale.getAdmin() != null ? sale.getAdmin().getFullName() : "N/A");
                row.createCell(4).setCellValue(sale.getItems());
                row.createCell(5).setCellValue(sale.getTotal().doubleValue());
                row.createCell(6).setCellValue(sale.getStatus().toString());

                totalItems += sale.getItems();
                totalAmount += sale.getTotal().doubleValue();
            }
            Row totalRow = sheet.createRow(rowIdx);
            totalRow.createCell(3).setCellValue("TOTAL");
            totalRow.createCell(4).setCellValue(totalItems);
            totalRow.createCell(5).setCellValue(totalAmount);

            // Resize columns to fit content
            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    /**
     * Generate sales report in PDF format
     * @param startDate the start date
     * @param endDate the end date
     * @return ByteArrayInputStream containing the PDF file
     */
    public ByteArrayInputStream generatePdfReport(String options, LocalDate startDate, LocalDate endDate) throws DocumentException {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        LocalDate today = LocalDate.now();

        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("options is empty");
        }
        switch (options.toLowerCase()) {
            case "daily":
                startDateTime = today.atStartOfDay();
                endDateTime = today.atTime(23, 59, 59);
                break;
            case "weekly":
                // Mulai dari hari Senin minggu ini sampai Minggu
                LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
                LocalDate endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);
                startDateTime = startOfWeek.atStartOfDay();
                endDateTime = endOfWeek.atTime(23, 59, 59);
                break;
            case "monthly":
                LocalDate firstDayOfMonth = today.withDayOfMonth(1);
                LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
                startDateTime = firstDayOfMonth.atStartOfDay();
                endDateTime = lastDayOfMonth.atTime(23, 59, 59);
                break;
            case "quarterly":
                int currentMonth = today.getMonthValue();
                int startMonth = ((currentMonth - 1) / 3) * 3 + 1;
                LocalDate startOfQuarter = LocalDate.of(today.getYear(), startMonth, 1);
                LocalDate endOfQuarter = startOfQuarter.plusMonths(2).withDayOfMonth(startOfQuarter.plusMonths(2).lengthOfMonth());
                startDateTime = startOfQuarter.atStartOfDay();
                endDateTime = endOfQuarter.atTime(23, 59, 59);
                break;
            case "yearly":
                LocalDate firstDayOfYear = today.withDayOfYear(1);
                LocalDate lastDayOfYear = today.withDayOfYear(today.lengthOfYear());
                startDateTime = firstDayOfYear.atStartOfDay();
                endDateTime = lastDayOfYear.atTime(23, 59, 59);
                break;
            case "custom":
                if (startDate == null || endDate == null) {
                    throw new IllegalArgumentException("startDate and endDate must not be null for custom option");
                }
                startDateTime = startDate.atStartOfDay();
                endDateTime = endDate.atTime(23, 59, 59);
                break;
            default:
                throw new IllegalArgumentException("Invalid report option: " + options);
        }

        List<Sale> sales = salesReportRepository.findSalesByDateRange(startDateTime, endDateTime);

        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add title
            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Sales Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Add date range
            com.itextpdf.text.Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph dateRange = new Paragraph(
                    "Period: " + startDateTime.format(DATE_FORMATTER) + " to " + endDateTime.format(DATE_FORMATTER),
                    dateFont);
            dateRange.setAlignment(Element.ALIGN_CENTER);
            document.add(dateRange);

            document.add(Chunk.NEWLINE);

            // Create table
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);

            // Add table headers
            com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Stream.of("Invoice Number", "Date", "Customer", "Admin", "Total Items", "Total Amount", "Status")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle, headerFont));
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(header);
                    });

            // Add data rows
            int totalItems = 0;
            double totalAmount = 0.0;
            for (Sale sale : sales) {
                table.addCell(sale.getInvoiceNumber());
                table.addCell(sale.getDate().format(DATE_FORMATTER));
                table.addCell(sale.getCustomer() != null ? sale.getCustomer().getNama() : "N/A");
                table.addCell(sale.getAdmin() != null ? sale.getAdmin().getFullName() : "N/A");
                table.addCell(String.valueOf(sale.getItems()));
                table.addCell(sale.getTotal().toString());
                table.addCell(sale.getStatus().toString());

                totalItems += sale.getItems();
                totalAmount += sale.getTotal().doubleValue();
            }
            PdfPCell totalCell = new PdfPCell(new Phrase("TOTAL"));
            totalCell.setColspan(4);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalCell);

            table.addCell(String.valueOf(totalItems));
            table.addCell(String.valueOf(totalAmount));
            table.addCell(""); // for status column

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            log.error("Error generating PDF report", e);
            throw e;
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
