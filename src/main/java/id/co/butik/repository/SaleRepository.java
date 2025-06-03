package id.co.butik.repository;

import id.co.butik.entity.Sale;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SaleRepository extends PagingAndSortingRepository<Sale, Long>, JpaSpecificationExecutor<Sale> {}
