package id.co.butik.repository;

import id.co.butik.entity.Sale;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.util.List;

public interface SaleRepository extends PagingAndSortingRepository<Sale, Long>, JpaSpecificationExecutor<Sale> {

    @Query("SELECT SUM(s.total) FROM Sale s")
    BigDecimal getTotalSales();

    long count();

    @Query("SELECT s FROM Sale s ORDER BY s.date DESC")
    List<Sale> findRecentSales(Pageable pageable);

    @Query("SELECT FUNCTION('MONTH', s.date) as month, SUM(s.items) as items FROM Sale s WHERE FUNCTION('YEAR', s.date) = FUNCTION('YEAR', CURRENT_DATE) GROUP BY FUNCTION('MONTH', s.date) ORDER BY FUNCTION('MONTH', s.date)")
    List<Object[]> getMonthlySalesForCurrentYear();

    @Query("SELECT SUM(s.total) FROM Sale s WHERE FUNCTION('DATE', s.date) = CURRENT_DATE")
    BigDecimal getTotalSalesToday();

    @Query("SELECT COUNT(s) FROM Sale s WHERE FUNCTION('DATE', s.date) = CURRENT_DATE")
    Long countSalesToday();

    @Query(value = "SELECT DATE(s.date) AS day, SUM(s.items) AS items FROM sale s  WHERE s.date >= CURRENT_DATE - INTERVAL '6 days' GROUP BY DATE(s.date) ORDER BY DATE(s.date)", nativeQuery = true)
    List<Object[]> getDailySalesForLastSevenDays();

}
