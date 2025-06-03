package id.co.butik.repository;

import id.co.butik.entity.SaleDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SaleDetailRepository extends PagingAndSortingRepository<SaleDetail, Long>, JpaSpecificationExecutor<SaleDetail> {}
