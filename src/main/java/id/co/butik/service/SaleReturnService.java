package id.co.butik.service;

import id.co.butik.entity.SaleReturn;
import id.co.butik.repository.SaleReturnRepository;
import id.co.butik.responseException.BadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SaleReturnService {

    @Autowired
    private SaleReturnRepository saleReturnRepository;

    public Page<SaleReturn> getSaleReturns(Specification<SaleReturn> specification, Pageable pageable) {
        return saleReturnRepository.findAll(specification, pageable);
    }

    public SaleReturn getSaleReturnById(Long id) {
        return saleReturnRepository.findById(id).orElseThrow(() -> new BadRequest("Sale Return with id not found"));
    }

    public SaleReturn createSaleReturn(SaleReturn saleReturn) {
        return saleReturnRepository.save(saleReturn);
    }

    public SaleReturn updateSaleReturn(Long id, SaleReturn saleReturn) {
        SaleReturn oldSaleReturn = getSaleReturnById(id);
        oldSaleReturn.setSale(saleReturn.getSale());
        oldSaleReturn.setAdmin(saleReturn.getAdmin());
        oldSaleReturn.setReason(saleReturn.getReason());
        oldSaleReturn.setTotalRefund(saleReturn.getTotalRefund());
        oldSaleReturn.setDetails(saleReturn.getDetails());
        return saleReturnRepository.save(oldSaleReturn);
    }

    public String deleteSaleReturn(Long id) {
        SaleReturn saleReturn = saleReturnRepository.findById(id).orElseThrow(() -> new BadRequest("Sale Return not found"));
        saleReturnRepository.deleteById(id);
        return "{\"success\":true}";
    }
}