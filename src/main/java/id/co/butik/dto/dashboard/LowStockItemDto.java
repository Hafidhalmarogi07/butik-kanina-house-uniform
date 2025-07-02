package id.co.butik.dto.dashboard;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for low stock item data
 */
@Data
public class LowStockItemDto implements Serializable {
    private Long productId;
    private String productName;
    private String productDescription;
    private String imageUrl;
    private int currentStock;
    private BigDecimal price;
    
    public LowStockItemDto() {
    }
    
    public LowStockItemDto(Long productId, String productName, String productDescription, 
                          String imageUrl, int currentStock, BigDecimal price) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.imageUrl = imageUrl;
        this.currentStock = currentStock;
        this.price = price;
    }
}