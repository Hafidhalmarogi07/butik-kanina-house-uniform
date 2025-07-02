package id.co.butik.dto.dashboard;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for stock movement data
 */
@Data
public class StockMovementDto implements Serializable {
    private String id;
    private String itemName;
    private String type; // "IN" or "OUT"
    private int quantity;
    private LocalDate date;
    
    public StockMovementDto() {
    }
    
    public StockMovementDto(String id, String itemName, String type, int quantity, LocalDate date) {
        this.id = id;
        this.itemName = itemName;
        this.type = type;
        this.quantity = quantity;
        this.date = date;
    }
}