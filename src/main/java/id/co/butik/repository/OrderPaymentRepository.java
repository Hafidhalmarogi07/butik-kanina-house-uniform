package id.co.butik.repository;

import id.co.butik.entity.OrderPayment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderPaymentRepository extends PagingAndSortingRepository<OrderPayment, Long>, JpaSpecificationExecutor<OrderPayment> {
}
