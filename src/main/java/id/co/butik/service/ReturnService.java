package id.co.butik.service;

import id.co.butik.dto.retur.ReturnItemRequest;
import id.co.butik.dto.retur.ReturnRequest;
import id.co.butik.entity.*;
import id.co.butik.entity.users.UserProfile;
import id.co.butik.enums.ReturnSourceType;
import id.co.butik.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class ReturnService {

    private final SaleReturnRepository returnRepository;
    private final ReturnDetailRepository returnDetailRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final SaleRepository saleRepository;
    private final UserProfileRepository userProfileRepository;

    public ReturnService(SaleReturnRepository returnRepository,
                         ReturnDetailRepository returnDetailRepository,
                         ProductRepository productRepository,
                         OrderRepository orderRepository, SaleRepository saleRepository,  UserProfileRepository userProfileRepository) {
        this.returnRepository = returnRepository;
        this.returnDetailRepository = returnDetailRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.saleRepository = saleRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional
    public SaleOrderReturn processReturn(ReturnRequest request, HttpServletRequest servletRequest) {
        SaleOrderReturn saleOrderReturn = new SaleOrderReturn();
        if(ReturnSourceType.ORDER.equals(request.getReturnSourceType())) {
            Order  order = orderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
           saleOrderReturn.setOrder(order);
        } else if (ReturnSourceType.SALE.equals(request.getReturnSourceType())) {
           Sale sale = saleRepository.findById(request.getSaleId()).orElseThrow(() -> new RuntimeException("Sale not found"));
           saleOrderReturn.setSale(sale);
        }
        UserProfile userProfile = userProfileRepository.findFirstByEmail(servletRequest.getRemoteUser());
        saleOrderReturn.setReturnDate(LocalDateTime.now());
        saleOrderReturn.setNotes(request.getNote());
        saleOrderReturn.setReturnType(request.getReturnType());
        saleOrderReturn.setSourceType(request.getReturnSourceType());
        saleOrderReturn.setAdmin(userProfile);
        SaleOrderReturn savedReturn = returnRepository.save(saleOrderReturn);

        for (ReturnItemRequest item : request.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            ReturnDetail detail = new ReturnDetail();
            detail.setSaleOrderReturn(savedReturn);
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setNote(item.getReason());
            returnDetailRepository.save(detail);

            // ✅ Tambah stok kembali
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        return savedReturn;
    }

    public Page<SaleOrderReturn> getAllReturns(Specification<SaleOrderReturn> spec, Pageable pageable) {
        return returnRepository.findAll(spec, pageable);
    }

    public SaleOrderReturn getById(Long id) {
        return returnRepository.findById(id).orElseThrow(() -> new RuntimeException("Return not found"));
    }
}
