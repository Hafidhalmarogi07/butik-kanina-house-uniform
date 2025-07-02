package id.co.butik.repository;

import id.co.butik.entity.Product;
import id.co.butik.entity.StockAlert;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface StockAlertRepository extends PagingAndSortingRepository<StockAlert, Long> , JpaSpecificationExecutor<StockAlert> {
    boolean existsByProductAndReasonAndResolvedFalse(Product product, String reason);
    List<StockAlert> findByResolvedFalse();
    List<StockAlert> findByProductAndReasonAndResolvedFalse(Product product, String reason);

}
