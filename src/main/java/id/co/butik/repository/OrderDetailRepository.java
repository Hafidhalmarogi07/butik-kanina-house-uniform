package id.co.butik.repository;

import id.co.butik.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderDetailRepository extends PagingAndSortingRepository<OrderDetail, Long>, JpaSpecificationExecutor<OrderDetail> {}
