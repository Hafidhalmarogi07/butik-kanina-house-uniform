package id.co.butik.repository;

import id.co.butik.entity.OrderReturn;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SaleReturnRepository extends PagingAndSortingRepository<OrderReturn, Long>, JpaSpecificationExecutor<OrderReturn> {}
