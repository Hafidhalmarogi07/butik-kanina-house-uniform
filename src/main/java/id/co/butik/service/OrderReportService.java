package id.co.butik.service;

import id.co.butik.entity.Order;
import id.co.butik.entity.OrderDetail;
import id.co.butik.repository.OrderReportRepository;
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
import java.util.stream.Stream;

@Service
@Slf4j
public class OrderReportService {

    @Autowired
    private OrderReportRepository orderReportRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public List<Order> getOrderReport(String options, LocalDate startDate, LocalDate endDate) {
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
       return orderReportRepository.findOrdersByDateRange(startDateTime, endDateTime);

    }
    /**
     * Generate order report in Excel format
     * @param options the report option (daily, weekly, monthly, quarterly, yearly, custom)
     * @param startDate the start date - required for custom option
     * @param endDate the end date - required for custom option
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
        List<Order> orders = orderReportRepository.findOrdersByDateRange(startDateTime, endDateTime);

        try (Workbook workbook = new XSSFWorkbook(); 
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Order Report");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Order Number");
            headerRow.createCell(1).setCellValue("Order Date");
            headerRow.createCell(2).setCellValue("Due Date");
            headerRow.createCell(3).setCellValue("Customer");
            headerRow.createCell(4).setCellValue("Admin");
            headerRow.createCell(5).setCellValue("Total Amount");
            headerRow.createCell(6).setCellValue("Amount Paid");
            headerRow.createCell(7).setCellValue("Order Status");
            headerRow.createCell(8).setCellValue("Payment Status");

            // Create data rows
            int rowIdx = 1;
            double totalAmount = 0;
            double totalPaid = 0;
            for (Order order : orders) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(order.getOrderNumber());
                row.createCell(1).setCellValue(order.getOrderDate().format(DATE_FORMATTER));
                row.createCell(2).setCellValue(order.getDueDate() != null ? order.getDueDate().toString() : "N/A");
                row.createCell(3).setCellValue(order.getCustomer() != null ? order.getCustomer().getNama() : "N/A");
                row.createCell(4).setCellValue(order.getAdmin() != null ? order.getAdmin().getFullName() : "N/A");
                row.createCell(5).setCellValue(order.getTotalAmount().doubleValue());
                row.createCell(6).setCellValue(order.getAmountPaid() != null ? order.getAmountPaid().doubleValue() : 0.0);
                row.createCell(7).setCellValue(order.getOrderStatus() != null ? order.getOrderStatus().toString() : "N/A");
                row.createCell(8).setCellValue(order.getPaymentStatus() != null ? order.getPaymentStatus().toString() : "N/A");

                totalAmount += order.getTotalAmount().doubleValue();
                totalPaid += order.getAmountPaid().doubleValue();
            }
            Row totalRow = sheet.createRow(rowIdx);
            totalRow.createCell(4).setCellValue("TOTAL");
            totalRow.createCell(5).setCellValue(totalAmount);
            totalRow.createCell(6).setCellValue(totalPaid);

            // Resize columns to fit content
            for (int i = 0; i < 9; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    /**
     * Generate order report in PDF format
     * @param options the report option (daily, weekly, monthly, quarterly, yearly, custom)
     * @param startDate the start date - required for custom option
     * @param endDate the end date - required for custom option
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
        List<Order> orders = orderReportRepository.findOrdersByDateRange(startDateTime, endDateTime);

        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add title
            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Order Report", titleFont);
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
            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);

            // Add table headers
            com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Stream.of("Order Number", "Order Date", "Due Date", "Customer", "Admin", "Total Amount", "Amount Paid", "Order Status", "Payment Status")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle, headerFont));
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(header);
                    });

            // Add data rows
            double totalAmount = 0.0;
            double totalPaid = 0.0;
            for (Order order : orders) {
                table.addCell(order.getOrderNumber());
                table.addCell(order.getOrderDate().format(DATE_FORMATTER));
                table.addCell(order.getDueDate() != null ? order.getDueDate().toString() : "N/A");
                table.addCell(order.getCustomer() != null ? order.getCustomer().getNama() : "N/A");
                table.addCell(order.getAdmin() != null ? order.getAdmin().getFullName() : "N/A");
                table.addCell(order.getTotalAmount().toString());
                table.addCell(order.getAmountPaid() != null ? order.getAmountPaid().toString() : "0");
                table.addCell(order.getOrderStatus() != null ? order.getOrderStatus().toString() : "N/A");
                table.addCell(order.getPaymentStatus() != null ? order.getPaymentStatus().toString() : "N/A");

                totalAmount += order.getTotalAmount().doubleValue();
                totalPaid += order.getAmountPaid().doubleValue();

            }

            PdfPCell totalCell = new PdfPCell(new Phrase("TOTAL"));
            totalCell.setColspan(5);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalCell);

            table.addCell(String.valueOf(totalAmount));
            table.addCell(String.valueOf(totalPaid));
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
