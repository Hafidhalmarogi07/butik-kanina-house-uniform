package id.co.butik.service;

import id.co.butik.entity.ReturnDetail;
import id.co.butik.repository.ReturnDetailRepository;
import id.co.butik.responseException.BadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReturnDetailService {

    @Autowired
    private ReturnDetailRepository returnDetailRepository;

    public Page<ReturnDetail> findAll(Specification<ReturnDetail> specification, Pageable pageable) {
        return returnDetailRepository.findAll(specification, pageable);
    }

    public ReturnDetail findById(Long id) {
        return returnDetailRepository.findById(id).orElseThrow(() -> new BadRequest("ReturnDetail with id not found"));
    }

    public ReturnDetail save(ReturnDetail returnDetail) {
        return returnDetailRepository.save(returnDetail);
    }

    public ReturnDetail update(Long id, ReturnDetail returnDetail) {
        ReturnDetail existingReturnDetail = findById(id);
        existingReturnDetail.setProduct(returnDetail.getProduct());
        existingReturnDetail.setSaleReturn(returnDetail.getSaleReturn());
        existingReturnDetail.setSize(returnDetail.getSize());
        existingReturnDetail.setQuantity(returnDetail.getQuantity());
        existingReturnDetail.setNote(returnDetail.getNote());
        return returnDetailRepository.save(existingReturnDetail);
    }

    public String deleteById(Long id) {
        ReturnDetail returnDetail = findById(id);
        returnDetailRepository.deleteById(id);
        return "{\"success\":true}";
    }
}