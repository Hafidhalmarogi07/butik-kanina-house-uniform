package id.co.butik.dto.dashboard;

import lombok.Data;
import java.io.Serializable;

/**
 * DTO for inventory movement data
 */
@Data
public class InventoryMovementDto implements Serializable {
    private String month;
    private long incomingStock;
    private long outgoingStock;
    
    public InventoryMovementDto() {
    }
    
    public InventoryMovementDto(String month, long incomingStock, long outgoingStock) {
        this.month = month;
        this.incomingStock = incomingStock;
        this.outgoingStock = outgoingStock;
    }
}