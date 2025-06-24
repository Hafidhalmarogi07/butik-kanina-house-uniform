package id.co.butik.dto.dashboard;

import id.co.butik.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentOrderDto {
    private Long id;
    private String orderNumber;
    private String customerName;
    private OrderStatus status;
    private BigDecimal amount;
}
