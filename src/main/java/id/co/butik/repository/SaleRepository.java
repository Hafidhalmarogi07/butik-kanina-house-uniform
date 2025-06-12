package id.co.butik.repository;

import id.co.butik.entity.Sale;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;

public interface SaleRepository extends PagingAndSortingRepository<Sale, Long>, JpaSpecificationExecutor<Sale> {

    @Query("SELECT SUM(s.total) FROM Sale s")
    BigDecimal getTotalSales();

    long count();
}
