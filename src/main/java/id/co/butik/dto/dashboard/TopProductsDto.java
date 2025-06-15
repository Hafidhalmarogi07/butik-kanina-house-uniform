package id.co.butik.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopProductsDto {
    private Long productId;
    private String productName;
    private String productDescription;
    private String imageUrl;
    private BigDecimal price;
    private Long totalSold;
}