package id.co.butik.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for daily sales data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailySalesDto {
    private LocalDate day;
    private String dayName;
    private Long items;
}