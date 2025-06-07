package id.co.butik.dto.retur;

import id.co.butik.enums.ReturnSourceType;
import id.co.butik.enums.ReturnType;
import lombok.Data;

import java.util.List;

@Data
public class ReturnRequest {
    private Long orderId;
    private Long saleId;
    private String note;
    private ReturnType returnType;
    private ReturnSourceType returnSourceType;
    private List<ReturnItemRequest> items;

}
