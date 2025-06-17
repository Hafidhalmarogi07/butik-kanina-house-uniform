package id.co.butik.controller;

import id.co.butik.service.ExpenseReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/expense-report")
public class ExpenseReportController {

    @Autowired
    private ExpenseReportService expenseReportService;

    /**
     * Generate expense report in Excel format
     * @param startDate the start date (format: yyyy-MM-dd'T'HH:mm:ss)
     * @param endDate the end date (format: yyyy-MM-dd'T'HH:mm:ss)
     * @return Excel file as response
     */
    @GetMapping(value = "/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> generateExcelReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) throws IOException {

        LocalDate startLocalDate = expenseReportService.convertToLocalDate(startDate);
        LocalDate endLocalDate = expenseReportService.convertToLocalDate(endDate);
        
        ByteArrayInputStream in = expenseReportService.generateExcelReport(startLocalDate, endLocalDate);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=expense_report.xlsx");
        
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    /**
     * Generate expense report in PDF format
     * @param startDate the start date (format: yyyy-MM-dd'T'HH:mm:ss)
     * @param endDate the end date (format: yyyy-MM-dd'T'HH:mm:ss)
     * @return PDF file as response
     */
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> generatePdfReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) throws Exception {

        LocalDate startLocalDate = expenseReportService.convertToLocalDate(startDate);
        LocalDate endLocalDate = expenseReportService.convertToLocalDate(endDate);
        
        ByteArrayInputStream in = expenseReportService.generatePdfReport(startLocalDate, endLocalDate);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=expense_report.pdf");
        
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }
}