package id.co.butik.service;


import id.co.butik.entity.Product;
import id.co.butik.entity.Production;
import id.co.butik.entity.StockAlert;
import id.co.butik.enums.ProductionStatus;
import id.co.butik.repository.ProductRepository;
import id.co.butik.repository.ProductionRepository;
import id.co.butik.repository.StockAlertRepository;
import id.co.butik.responseException.BadRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class ProductionService {
    private final ProductionRepository repo;
    private final ProductRepository productRepository;
    private final StockAlertRepository stockAlertRepository;


    public ProductionService(ProductionRepository repo, ProductRepository productRepository, StockAlertRepository stockAlertRepository) {
        this.repo = repo;
        this.productRepository = productRepository;
        this.stockAlertRepository = stockAlertRepository;
    }

    public Page<Production> findAll(Specification<Production> spec, Pageable pageable) {
        return repo.findAll(spec, pageable);
    }

    public Production findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new BadRequest("Production with id not found "));
    }

    @Transactional
    public Production createProduction(Production production) {
        if(null != production.getProduct() && null != production.getProduct().getId()) {
            production.setProduct(productRepository.findById(production.getProduct().getId()).orElseThrow(() -> new BadRequest("Product not found ")));
        }
        if(ProductionStatus.FINISHED.equals(production.getStatus())) {
            production.setProgress(100);
            Product oldProduct = production.getProduct();
            if (oldProduct == null) throw new AssertionError();
            oldProduct.setStock(oldProduct.getStock() + production.getQuantity());
            productRepository.save(oldProduct);
            updateStockAlertAfterProduction(oldProduct, production.getQuantity());
        }
        return repo.save(production);
    }

    @Transactional
    public Production updateProduction(Long id, Production production) {
            Production oldProduction = findById(id);
            if(null != production.getProduct() && null != production.getProduct().getId()) {
                oldProduction.setProduct(productRepository.findById(production.getProduct().getId()).orElseThrow(() -> new BadRequest("Product not found ")));
            }
            if(ProductionStatus.FINISHED.equals(production.getStatus())) {
                production.setProgress(100);
                Product oldProduct = oldProduction.getProduct();
                if (oldProduct == null) throw new AssertionError();
                if(oldProduction.getStatus().equals(ProductionStatus.FINISHED)) {
                    oldProduct.setStock(oldProduct.getStock() - oldProduction.getQuantity() + production.getQuantity());
                }else {
                    oldProduct.setStock(oldProduct.getStock() + production.getQuantity());
                }
                productRepository.save(oldProduct);
                updateStockAlertAfterProduction(oldProduct, production.getQuantity());
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

    public void updateStockAlertAfterProduction(Product product, int producedQty) {
        List<StockAlert> alerts = stockAlertRepository.findByProductAndResolvedFalse(product);
        if(null != alerts){
            for (StockAlert alert : alerts) {

                int remaining = alert.getNeededQuantity() - producedQty;
            

                if (remaining <= 0) {
                    alert.setNeededQuantity(0);
                    alert.setResolved(true);
                } else {
                    alert.setNeededQuantity(remaining);
                }

                stockAlertRepository.save(alert);
            }
        }
    }
}