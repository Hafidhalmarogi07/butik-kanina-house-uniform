package id.co.butik.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for monthly sales data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySalesDto {
    private String month;
    private BigDecimal amount;
}