package id.co.butik.service;

import id.co.butik.entity.SaleOrderReturn;
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

    public Page<SaleOrderReturn> getSaleReturns(Specification<SaleOrderReturn> specification, Pageable pageable) {
        return saleReturnRepository.findAll(specification, pageable);
    }

    public SaleOrderReturn getSaleReturnById(Long id) {
        return saleReturnRepository.findById(id).orElseThrow(() -> new BadRequest("Sale Return with id not found"));
    }

    public SaleOrderReturn createSaleReturn(SaleOrderReturn saleOrderReturn) {
        return saleReturnRepository.save(saleOrderReturn);
    }

    public SaleOrderReturn updateSaleReturn(Long id, SaleOrderReturn saleOrderReturn) {
        SaleOrderReturn oldSaleOrderReturn = getSaleReturnById(id);
        oldSaleOrderReturn.setSale(saleOrderReturn.getSale());
        oldSaleOrderReturn.setAdmin(saleOrderReturn.getAdmin());
        oldSaleOrderReturn.setReason(saleOrderReturn.getReason());
        oldSaleOrderReturn.setTotalRefund(saleOrderReturn.getTotalRefund());
        oldSaleOrderReturn.setDetails(saleOrderReturn.getDetails());
        return saleReturnRepository.save(oldSaleOrderReturn);
    }

    public String deleteSaleReturn(Long id) {
        SaleOrderReturn saleOrderReturn = saleReturnRepository.findById(id).orElseThrow(() -> new BadRequest("Sale Return not found"));
        saleReturnRepository.deleteById(id);
        return "{\"success\":true}";
    }
}