package id.co.butik.service;

import id.co.butik.entity.OrderDetail;
import id.co.butik.repository.OrderDetailRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderDetailService {
    private final OrderDetailRepository repo;

    public OrderDetailService(OrderDetailRepository repo) {
        this.repo = repo;
    }

    public Page<OrderDetail> findAll(Specification<OrderDetail> spec, Pageable pageable) {
        return repo.findAll(spec, pageable);
    }

    public Optional<OrderDetail> findById(Long id) {
        return repo.findById(id);
    }

    public OrderDetail save(OrderDetail obj) {
        return repo.save(obj);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}