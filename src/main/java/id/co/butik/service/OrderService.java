package id.co.butik.service;

import id.co.butik.entity.Order;
import id.co.butik.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository repo;

    public OrderService(OrderRepository repo) {
        this.repo = repo;
    }

    public Page<Order> findAll(Specification<Order> spec, Pageable pageable) {
        return repo.findAll(spec, pageable);
    }

    public Optional<Order> findById(Long id) {
        return repo.findById(id);
    }

    public Order save(Order obj) {
        return repo.save(obj);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
