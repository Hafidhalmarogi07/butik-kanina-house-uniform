package id.co.butik.dto.dashboard;

import lombok.Data;

@Data
public class ProductCountByCategoryDto {
    private String categoryName;
    private Long productCount;

    public ProductCountByCategoryDto(String categoryName, Long productCount) {
        this.categoryName = categoryName;
        this.productCount = productCount;
    }

}
