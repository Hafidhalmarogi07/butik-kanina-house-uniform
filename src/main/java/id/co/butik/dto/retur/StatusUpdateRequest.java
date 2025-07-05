package id.co.butik.dto.retur;

import id.co.butik.enums.ReturnStatus;
import lombok.Data;

@Data
public class StatusUpdateRequest {
    private ReturnStatus status;
}