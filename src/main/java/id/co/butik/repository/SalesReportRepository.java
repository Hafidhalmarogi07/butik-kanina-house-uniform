package id.co.butik.repository;

import id.co.butik.entity.Sale;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SalesReportRepository extends PagingAndSortingRepository<Sale, Long>, JpaSpecificationExecutor<Sale> {

    /**
     * Find sales within a date range
     * @param startDate the start date
     * @param endDate the end date
     * @return list of sales within the date range
     */
    @Query("SELECT s FROM Sale s WHERE s.date >= :startDate AND s.date <= :endDate ORDER BY s.date")
    List<Sale> findSalesByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}