package id.co.butik.repository;

import id.co.butik.entity.Order;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long>, JpaSpecificationExecutor<Order> {


    @Query("SELECT SUM(o.totalAmount) FROM  Order o")
    BigDecimal getTotalOrder();
}
