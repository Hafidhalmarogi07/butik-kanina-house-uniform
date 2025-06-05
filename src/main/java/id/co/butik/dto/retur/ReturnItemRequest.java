package id.co.butik.dto.retur;

import lombok.Data;

@Data
public class ReturnItemRequest {
    private Long productId;
    private Integer quantity;
    private String reason;
}