package id.co.butik.repository;

import id.co.butik.entity.Production;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductionRepository extends PagingAndSortingRepository<Production, Long>, JpaSpecificationExecutor<Production> {}
