package id.co.butik.dto.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailRequest {
    private Long productId;
    private int quantity;
    private BigDecimal unitPrice;
}