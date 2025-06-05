package id.co.butik.service;

import id.co.butik.dto.retur.ReturnRequest;
import id.co.butik.entity.Order;
import id.co.butik.entity.ReturnDetail;
import id.co.butik.entity.SaleReturn;
import id.co.butik.repository.OrderRepository;
import id.co.butik.repository.ProductRepository;
import id.co.butik.repository.ReturnDetailRepository;
import id.co.butik.repository.SaleReturnRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ReturnService {

    private final SaleReturnRepository returnRepository;
    private final ReturnDetailRepository returnDetailRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public ReturnService(SaleReturnRepository returnRepository,
                         ReturnDetailRepository returnDetailRepository,
                         ProductRepository productRepository,
                         OrderRepository orderRepository) {
        this.returnRepository = returnRepository;
        this.returnDetailRepository = returnDetailRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

//    @Transactional
//    public SaleReturn processReturn(ReturnRequest request) {
//        Order order = orderRepository.findById(request.getOrderId())
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        SaleReturn saleReturn = new SaleReturn();
//        saleReturn.setOrder(order);
//        saleReturn.setReturnDate(LocalDate.now());
//        saleReturn.setNote(request.getNote());
//        SaleReturn savedReturn = returnRepository.save(saleReturn);
//
//        for (ReturnRequest.ReturnItemRequest item : request.getItems()) {
//            Product product = productRepository.findById(item.getProductId())
//                    .orElseThrow(() -> new RuntimeException("Product not found"));
//
//            ReturnDetail detail = new ReturnDetail();
//            detail.setSaleReturn(savedReturn);
//            detail.setProduct(product);
//            detail.setQuantity(item.getQuantity());
//            detail.setReason(item.getReason());
//            returnDetailRepository.save(detail);
//
//            // ✅ Tambah stok kembali
//            product.setStock(product.getStock() + item.getQuantity());
//            productRepository.save(product);
//        }
//
//        return savedReturn;
//    }
//
//    public List<SaleReturn> getAllReturns() {
//        return returnRepository.findAll();
//    }
//
//    public Optional<SaleReturn> getById(Long id) {
//        return returnRepository.findById(id);
//    }
}
