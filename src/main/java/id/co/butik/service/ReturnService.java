package id.co.butik.service;

import id.co.butik.dto.retur.ReturnItemRequest;
import id.co.butik.dto.retur.ReturnRequest;
import id.co.butik.entity.*;
import id.co.butik.entity.users.UserProfile;
import id.co.butik.enums.ReturnStatus;
import id.co.butik.enums.ReturnType;
import id.co.butik.repository.*;
import id.co.butik.responseException.BadRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;
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
    public OrderReturn processReturn(ReturnRequest request, HttpServletRequest servletRequest) {
        OrderReturn orderReturn = new OrderReturn();
        if(request.getOrderNumber().isEmpty()) {
            throw new BadRequest("Order number not found");
        }
        Order  order = orderRepository.findOneByOrderNumber(request.getOrderNumber());
        if(null == order) {
            throw new BadRequest("Order not found");
        }
        orderReturn.setOrder(order);

        UserProfile userProfile = userProfileRepository.findFirstByEmail(servletRequest.getRemoteUser());
        orderReturn.setReturnDate(LocalDateTime.now());
        orderReturn.setReason(request.getReason());
        orderReturn.setReturnType(request.getReturnType());
        orderReturn.setAdmin(userProfile);
        orderReturn.setStatusReturn(ReturnStatus.PENDING);
        orderReturn.setTotalRefund(new BigDecimal("0"));
        OrderReturn savedReturn = returnRepository.save(orderReturn);
        for (ReturnItemRequest item : request.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            ReturnDetail detail = new ReturnDetail();
            detail.setOrderReturn(savedReturn);
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setNote(item.getReason());
            detail.getOrderReturn().setTotalRefund(detail.getOrderReturn().getTotalRefund().add(product.getSellingPrice().multiply(BigDecimal.valueOf(item.getQuantity()))));
            returnDetailRepository.save(detail);
            if(ReturnType.REFUND.equals(request.getReturnType())) {
                // ✅ Tambah stok kembali
                product.setStock(product.getStock() + item.getQuantity());
                productRepository.save(product);
            }
        }

        return savedReturn;
    }

    public Page<OrderReturn> getAllReturns(Specification<OrderReturn> spec, Pageable pageable) {
        return returnRepository.findAll(spec, pageable);
    }

    public OrderReturn getById(Long id) {
        return returnRepository.findById(id).orElseThrow(() -> new RuntimeException("Return not found"));
    }
}
