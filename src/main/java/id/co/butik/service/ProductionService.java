package id.co.butik.service;


import id.co.butik.entity.Production;
import id.co.butik.repository.ProductRepository;
import id.co.butik.repository.ProductionRepository;
import id.co.butik.responseException.BadRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class ProductionService {
    private final ProductionRepository repo;
    private final ProductRepository productRepository;


    public ProductionService(ProductionRepository repo, ProductRepository productRepository) {
        this.repo = repo;
        this.productRepository = productRepository;
    }

    public Page<Production> findAll(Specification<Production> spec, Pageable pageable) {
        return repo.findAll(spec, pageable);
    }

    public Production findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new BadRequest("Production with id not found "));
    }

    public Production createProduction(Production production) {
        if(null != production.getProduct() && null != production.getProduct().getId()) {
            production.setProduct(productRepository.findById(production.getProduct().getId()).orElseThrow(() -> new BadRequest("Product not found ")));
        }
        return repo.save(production);
    }

    public Production updateProduction(Long id, Production production) {
            Production oldProduction = findById(id);
            if(null != production.getProduct() && null != production.getProduct().getId()) {
                oldProduction.setProduct(productRepository.findById(production.getProduct().getId()).orElseThrow(() -> new BadRequest("Product not found ")));
            }
            oldProduction.setQuantity(production.getQuantity());
            oldProduction.setProgress(production.getProgress());
            oldProduction.setStatus(production.getStatus());
            oldProduction.setDescription(production.getDescription());
            oldProduction.setStartDate(production.getStartDate());
            oldProduction.setEndDate(production.getEndDate());
            return repo.save(oldProduction);
    }

    public String deleteProduction(Long id) {
        findById(id);
        repo.deleteById(id);
        return "{\"success\":true}";
    }
}