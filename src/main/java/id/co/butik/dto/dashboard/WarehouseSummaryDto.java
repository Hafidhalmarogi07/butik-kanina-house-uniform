package id.co.butik.dto.dashboard;

import lombok.Data;
import java.io.Serializable;

/**
 * DTO for warehouse summary data
 */
@Data
public class WarehouseSummaryDto implements Serializable {
    private long totalInventory;
    private long incomingStock;
    private long outgoingStock;
    private long lowStockItems;
}