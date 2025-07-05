package id.co.butik.repository;

import id.co.butik.entity.Order;
import id.co.butik.enums.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long>, JpaSpecificationExecutor<Order> {


    @Query("SELECT SUM(o.totalAmount) FROM  Order o")
    BigDecimal getTotalOrder();

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE FUNCTION('DATE', o.orderDate) = CURRENT_DATE")
    BigDecimal getTotalOrderToday();


    @Query("SELECT COUNT(o) FROM Order o WHERE FUNCTION('DATE', o.orderDate) = CURRENT_DATE")
    Long countOrderToday();

    @Query("SELECT COUNT(o) FROM Order o WHERE FUNCTION('DATE', o.orderDate) = CURRENT_DATE  AND o.orderStatus = :orderStatus")
    Long countOrderTodayAndOrderStatus(OrderStatus orderStatus);

    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findRecentOrder(Pageable pageable);

    Order findOneByOrderNumber(String orderNumber);

}
