package id.co.butik.dto.dashboard;

import lombok.Data;
import java.io.Serializable;

/**
 * DTO for warehouse capacity data
 */
@Data
public class WarehouseCapacityDto implements Serializable {
    private long usedSpace;
    private long availableSpace;
    
    public WarehouseCapacityDto() {
    }
    
    public WarehouseCapacityDto(long usedSpace, long availableSpace) {
        this.usedSpace = usedSpace;
        this.availableSpace = availableSpace;
    }
}