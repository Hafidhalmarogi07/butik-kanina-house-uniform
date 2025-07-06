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
    private String description; // Description of the movement (e.g., "Produksi", "Penjualan", "Order keluar")
    private String movementType; // Type of movement (Production, Sale, Order)

    public StockMovementDto() {
    }

    public StockMovementDto(String id, String itemName, String type, int quantity, LocalDate date) {
        this.id = id;
        this.itemName = itemName;
        this.type = type;
        this.quantity = quantity;
        this.date = date;
    }

    public StockMovementDto(String id, String itemName, String type, int quantity, LocalDate date, String description, String movementType) {
        this.id = id;
        this.itemName = itemName;
        this.type = type;
        this.quantity = quantity;
        this.date = date;
        this.description = description;
        this.movementType = movementType;
    }
}
