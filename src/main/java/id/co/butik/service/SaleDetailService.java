package id.co.butik.service;

import id.co.butik.entity.SaleDetail;
import id.co.butik.repository.SaleDetailRepository;
import id.co.butik.responseException.BadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SaleDetailService {

    @Autowired
    private SaleDetailRepository saleDetailRepository;

    public Page<SaleDetail> getSaleDetails(Specification<SaleDetail> var1, Pageable var2) {
        return saleDetailRepository.findAll(var1, var2);
    }

    public SaleDetail getSaleDetailById(Long id) {
        return saleDetailRepository.findById(id).orElseThrow(() -> new BadRequest("Sale Detail with id not found "));
    }

    public SaleDetail createSaleDetail(SaleDetail saleDetail) {
        return saleDetailRepository.save(saleDetail);
    }

    public SaleDetail updateSaleDetail(Long id, SaleDetail saleDetail) {
        SaleDetail oldSaleDetail = getSaleDetailById(id);
        oldSaleDetail.setSale(saleDetail.getSale());
        oldSaleDetail.setProduct(saleDetail.getProduct());
        oldSaleDetail.setQuantity(saleDetail.getQuantity());
        oldSaleDetail.setSubtotal(saleDetail.getSubtotal());
        return saleDetailRepository.save(oldSaleDetail);
    }

    public String deleteSaleDetail(Long id) {
        SaleDetail saleDetail = saleDetailRepository.findById(id).orElseThrow(() -> new BadRequest("Sale Detail not found "));
        saleDetailRepository.deleteById(id);
        return "{\"success\":true}";
    }
}
