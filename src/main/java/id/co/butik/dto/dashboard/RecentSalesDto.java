package id.co.butik.dto.dashboard;

import id.co.butik.enums.SaleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecentSalesDto {
    private Long id;
    private String invoiceNumber;
    private String itemName;
    private SaleStatus status;
    private BigDecimal amount;
}