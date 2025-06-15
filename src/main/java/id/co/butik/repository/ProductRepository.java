package id.co.butik.repository;

import id.co.butik.dto.dashboard.ProductCountByCategoryDto;
import id.co.butik.dto.dashboard.TopProductsDto;
import id.co.butik.entity.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> , JpaSpecificationExecutor<Product> {

    @Query("SELECT new id.co.butik.dto.dashboard.ProductCountByCategoryDto(c.name, COUNT(p)) " +
            "FROM Product p JOIN p.category c GROUP BY c.name")
    List<ProductCountByCategoryDto> countProductsByCategory();

    @Query("SELECT new id.co.butik.dto.dashboard.TopProductsDto(p.id, p.name, p.description, p.imageUrl, p.sellingPrice, SUM(sd.quantity)) " +
            "FROM SaleDetail sd JOIN sd.product p " +
            "GROUP BY p.id, p.name, p.description, p.imageUrl, p.sellingPrice " +
            "ORDER BY SUM(sd.quantity) DESC")
    List<TopProductsDto> findTopProducts();

    @Query(value = "SELECT new id.co.butik.dto.dashboard.TopProductsDto(p.id, p.name, p.description, p.imageUrl, p.sellingPrice, SUM(sd.quantity)) " +
            "FROM SaleDetail sd JOIN sd.product p " +
            "GROUP BY p.id, p.name, p.description, p.imageUrl, p.sellingPrice " +
            "ORDER BY SUM(sd.quantity) DESC")
    List<TopProductsDto> findTopProductsLimit(@Param("limit") int limit);
}
