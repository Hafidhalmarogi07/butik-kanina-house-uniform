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
import java.util.Optional;

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
    public SaleReturn processReturn(ReturnRequest request, HttpServletRequest servletRequest) {
        Order order = new Order();
        Sale sale = new Sale();
        if(ReturnSourceType.ORDER.equals(request.getReturnSourceType())) {
           order = orderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
        } else if (ReturnSourceType.SALE.equals(request.getReturnSourceType())) {
            sale = saleRepository.findById(request.getSaleId()).orElseThrow(() -> new RuntimeException("Sale not found"));
        }
        UserProfile userProfile = userProfileRepository.findFirstByEmail(servletRequest.getRemoteUser());
        SaleReturn saleReturn = new SaleReturn();
        saleReturn.setOrder(order);
        saleReturn.setSale(sale);
        saleReturn.setReturnDate(LocalDateTime.now());
        saleReturn.setNotes(request.getNote());
        saleReturn.setReturnType(request.getReturnType());
        saleReturn.setSourceType(request.getReturnSourceType());
        saleReturn.setAdmin(userProfile);
        SaleReturn savedReturn = returnRepository.save(saleReturn);

        for (ReturnItemRequest item : request.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            ReturnDetail detail = new ReturnDetail();
            detail.setSaleReturn(savedReturn);
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

    public Page<SaleReturn> getAllReturns(Specification<SaleReturn> spec, Pageable pageable) {
        return returnRepository.findAll(spec, pageable);
    }

    public Optional<SaleReturn> getById(Long id) {
        return returnRepository.findById(id);
    }
}
