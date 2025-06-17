package id.co.butik.repository;

import id.co.butik.entity.Order;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderReportRepository extends PagingAndSortingRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    /**
     * Find orders within a date range
     * @param startDate the start date
     * @param endDate the end date
     * @return list of orders within the date range
     */
    @Query("SELECT o FROM Order o WHERE o.orderDate >= :startDate AND o.orderDate <= :endDate ORDER BY o.orderDate")
    List<Order> findOrdersByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}