package id.co.butik.dto.retur;

import id.co.butik.enums.ReturnType;
import lombok.Data;

import java.util.List;

@Data
public class ReturnRequest {
    private String orderNumber;
    private Long saleId;
    private String reason;
    private ReturnType returnType;
    private List<ReturnItemRequest> items;

}
