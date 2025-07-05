package id.co.butik.service;

import id.co.butik.entity.OrderReturn;
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

    public Page<OrderReturn> getSaleReturns(Specification<OrderReturn> specification, Pageable pageable) {
        return saleReturnRepository.findAll(specification, pageable);
    }

    public OrderReturn getSaleReturnById(Long id) {
        return saleReturnRepository.findById(id).orElseThrow(() -> new BadRequest("Sale Return with id not found"));
    }

    public OrderReturn createSaleReturn(OrderReturn orderReturn) {
        return saleReturnRepository.save(orderReturn);
    }

    public OrderReturn updateSaleReturn(Long id, OrderReturn orderReturn) {
        OrderReturn oldOrderReturn = getSaleReturnById(id);
        oldOrderReturn.setAdmin(orderReturn.getAdmin());
        oldOrderReturn.setReason(orderReturn.getReason());
        oldOrderReturn.setTotalRefund(orderReturn.getTotalRefund());
        oldOrderReturn.setDetails(orderReturn.getDetails());
        oldOrderReturn.setStatusReturn(orderReturn.getStatusReturn());
        return saleReturnRepository.save(oldOrderReturn);
    }

    public String deleteSaleReturn(Long id) {
        saleReturnRepository.findById(id).orElseThrow(() -> new BadRequest("Sale Return not found"));
        saleReturnRepository.deleteById(id);
        return "{\"success\":true}";
    }
}