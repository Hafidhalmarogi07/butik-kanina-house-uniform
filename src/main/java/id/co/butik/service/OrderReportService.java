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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class OrderReportService {

    @Autowired
    private OrderReportRepository orderReportRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Generate order report in Excel format
     * @param startDate the start date
     * @param endDate the end date
     * @return ByteArrayInputStream containing the Excel file
     */
    public ByteArrayInputStream generateExcelReport(LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        List<Order> orders = orderReportRepository.findOrdersByDateRange(startDate, endDate);

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
            }

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
     * @param startDate the start date
     * @param endDate the end date
     * @return ByteArrayInputStream containing the PDF file
     */
    public ByteArrayInputStream generatePdfReport(LocalDateTime startDate, LocalDateTime endDate) throws DocumentException {
        List<Order> orders = orderReportRepository.findOrdersByDateRange(startDate, endDate);

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
                    "Period: " + startDate.format(DATE_FORMATTER) + " to " + endDate.format(DATE_FORMATTER),
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
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            log.error("Error generating PDF report", e);
            throw e;
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}