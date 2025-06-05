package id.co.butik.dto.retur;

import lombok.Data;

import java.util.List;

@Data
public class ReturnRequest {
    private Long orderId;
    private String note;
    private List<ReturnItemRequest> items;

}
