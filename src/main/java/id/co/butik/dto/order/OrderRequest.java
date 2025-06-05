package id.co.butik.dto.order;

import id.co.butik.enums.OrderStatus;
import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequest {
    private Long customerId;
    private LocalDateTime orderDate;
    private LocalDate dueDate;
    private OrderStatus status;
    private List<OrderDetailRequest> details;
    private List<OrderPaymentRequest> payments;
}


