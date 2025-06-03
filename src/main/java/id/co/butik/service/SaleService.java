package id.co.butik.service;

import id.co.butik.entity.Sale;
import id.co.butik.repository.SaleRepository;
import id.co.butik.responseException.BadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    public Page<Sale> getSales(Specification<Sale> var1, Pageable var2) {
        return saleRepository.findAll(var1, var2);
    }

    public Sale getSaleById(Long id) {
        return saleRepository.findById(id).orElseThrow(() -> new BadRequest("Sale with id not found "));
    }

    public Sale createSale(Sale sale) {
        return saleRepository.save(sale);
    }

    public Sale updateSale(Long id, Sale sale) {
        Sale oldSale = getSaleById(id);
        oldSale.setDate(sale.getDate());
        oldSale.setCustomer(sale.getCustomer());
        oldSale.setAdmin(sale.getAdmin());
        oldSale.setTotal(sale.getTotal());
        oldSale.setDetails(sale.getDetails());
        return saleRepository.save(oldSale);
    }

    public String deleteSale(Long id) {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new BadRequest("Sale not found "));
        saleRepository.deleteById(id);
        return "{\"success\":true}";
    }
}
