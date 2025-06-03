package id.co.butik.repository;

import id.co.butik.entity.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> , JpaSpecificationExecutor<Product> {
}
