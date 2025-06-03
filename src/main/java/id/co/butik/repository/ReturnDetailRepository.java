package id.co.butik.repository;

import id.co.butik.entity.ReturnDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReturnDetailRepository extends PagingAndSortingRepository<ReturnDetail, Long>, JpaSpecificationExecutor<ReturnDetail> {}
