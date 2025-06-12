package id.co.butik.repository;

import id.co.butik.dto.dashboard.ProductCountByCategoryDto;
import id.co.butik.entity.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> , JpaSpecificationExecutor<Product> {

    @Query("SELECT new id.co.butik.dto.dashboard.ProductCountByCategoryDto(c.name, COUNT(p)) " +
            "FROM Product p JOIN p.category c GROUP BY c.name")
    List<ProductCountByCategoryDto> countProductsByCategory();

}
