package id.co.butik.dto.order;

import id.co.butik.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderStatusUpdateRequest {
    private OrderStatus status;
}