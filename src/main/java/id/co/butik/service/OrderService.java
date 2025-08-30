package id.co.butik.service;

import id.co.butik.dto.order.OrderDetailRequest;
import id.co.butik.dto.order.OrderPaymentRequest;
import id.co.butik.dto.order.OrderRequest;
import id.co.butik.entity.*;
import id.co.butik.entity.users.UserProfile;
import id.co.butik.enums.OrderStatus;
import id.co.butik.enums.PaymentStatus;
import id.co.butik.repository.*;
import id.co.butik.responseException.BadRequest;
import id.co.butik.util.ImageUtils;
import id.co.butik.util.NumberGenerator;
import id.co.butik.util.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderPaymentRepository orderPaymentRepository;
    private final UserProfileRepository userProfileRepository;
    private final StockAlertRepository stockAlertRepository;
    private NumberGenerator numberGenerator;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository,
                        ProductRepository productRepository,
                        OrderPaymentRepository orderPaymentRepository, UserProfileRepository userProfileRepository, NumberGenerator numberGenerator, StockAlertRepository stockAlertRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderPaymentRepository = orderPaymentRepository;
        this.userProfileRepository = userProfileRepository;
        this.numberGenerator = numberGenerator;
        this.stockAlertRepository = stockAlertRepository;
    }


    public Page<Order> findAll(Specification<Order> spec, Pageable pageable) {

        Page<Order> orders = orderRepository.findAll(spec, pageable);
        for (Order order : orders){
           for (OrderPayment orderPayment : order.getPayments()){
               orderPayment.setImageUrl(PropertiesUtils.CDN_BASEURL+orderPayment.getImageUrl());
           }
        }
        return orderRepository.findAll(spec, pageable);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Order save(Order obj) {
        return orderRepository.save(obj);
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }


    @Transactional
    public Order createOrder(OrderRequest request, HttpServletRequest httpServletRequest) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        UserProfile userProfile = userProfileRepository.findFirstByEmail(httpServletRequest.getUserPrincipal().getName());

        Order order = new Order();
        order.setAdmin(userProfile);
        order.setCustomer(customer);
        order.setOrderDate(request.getOrderDate());
        order.setDueDate(request.getDueDate());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderNumber(numberGenerator.generateOrderNumber());

        List<OrderDetail> detailList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderDetailRequest detailReq : request.getDetails()) {
            Product product = productRepository.findById(detailReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(detailReq.getQuantity());
            detail.setPrice(detailReq.getUnitPrice());
            detail.setSubtotal(product.getSellingPrice().multiply(BigDecimal.valueOf(detailReq.getQuantity())));
            detailList.add(detail);
            totalAmount = totalAmount.add(product.getSellingPrice().multiply(BigDecimal.valueOf(detailReq.getQuantity())));

            //check stock alerts
            checkStockAndCreateAlerts(detail);

        }

        order.setTotalAmount(totalAmount);
        order.setAmountPaid(BigDecimal.ZERO); // akan dihitung dari payments
        order.setDetails(detailList);

        // Handle pembayaran awal (DP)
        BigDecimal totalPaid = BigDecimal.ZERO;
        if (request.getPayments() != null) {
            List<OrderPayment> payments = new ArrayList<>();
            for (OrderPaymentRequest payReq : request.getPayments()) {
                OrderPayment payment = new OrderPayment();
                payment.setOrder(order);
                payment.setPaymentDate(payReq.getPaymentDate());
                payment.setAmount(payReq.getAmount());
                payment.setPaymentMethod(payReq.getPaymentMethod());
                payment.setNotes(payReq.getNote());
                if(payReq.getImageUrl().isEmpty()) throw new BadRequest("bukti pembayaran dibutuhkan");
                payment.setImageUrl(ImageUtils.fromBase64(payReq.getImageUrl(), PropertiesUtils.CDN_PATH+"/payment", "/payment"));


                totalPaid = totalPaid.add(payment.getAmount());
                payments.add(payment);
            }
            order.setPayments(payments);
        }

        order.setAmountPaid(totalPaid);
        order.setPaymentStatus(
                totalPaid.compareTo(totalAmount) >= 0 ?
                        PaymentStatus.PAID :
                        PaymentStatus.PARTIALLY_PAID
        );

        return orderRepository.save(order);
    }

    public Order addPayment(Long orderId, OrderPaymentRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderPayment payment = new OrderPayment();
        payment.setOrder(order);
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setNotes(request.getNote());
        System.out.println("ini image request : "+request.getImageUrl());
        if(request.getImageUrl().isEmpty()) throw new BadRequest("bukti pembayaran dibutuhkan");
        payment.setImageUrl(ImageUtils.fromBase64(request.getImageUrl(), PropertiesUtils.CDN_PATH+"/payment", "/payment"));
        System.out.println("ini image Payment : " +payment.getImageUrl());


        orderPaymentRepository.save(payment);

        BigDecimal newPaid = order.getAmountPaid().add(payment.getAmount());
        order.setAmountPaid(newPaid);
        order.setPaymentStatus(
                newPaid.compareTo(order.getTotalAmount()) >= 0
                        ? PaymentStatus.PAID
                        : PaymentStatus.PARTIALLY_PAID
        );

        return orderRepository.save(order);
    }

    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(status);
        if(OrderStatus.COMPLETED.equals(status)){
            for(OrderDetail detail : order.getDetails()) {
                detail.getProduct().setStock(detail.getProduct().getStock() - detail.getQuantity());
            }
        }
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, OrderRequest request) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(request.getStatus());
        order.setOrderDate(request.getOrderDate());
        order.setDueDate(request.getDueDate());
        if(OrderStatus.COMPLETED.equals(request.getStatus())){
            for(OrderDetail detail : order.getDetails()) {
                detail.getProduct().setStock(detail.getProduct().getStock() - detail.getQuantity());
            }
        }

        return orderRepository.save(order);
    }

    public void checkStockAndCreateAlerts(OrderDetail detail) {
        Product product = productRepository.findById(detail.getProduct().getId()).orElseThrow(() -> new RuntimeException("Order not found"));
        int stock = product.getStock();
        int required = detail.getQuantity();

        if (stock < required) {
            int deficit = required - stock;

            // Cek apakah sudah ada alert yang belum selesai untuk produk ini
            List<StockAlert> existingAlertOpt = stockAlertRepository.findByProductAndReasonAndResolvedFalse(product, "ORDER_DEFICIT");

            if (!existingAlertOpt.isEmpty()) {
                StockAlert existingAlert = existingAlertOpt.get(0);
                existingAlert.setNeededQuantity(existingAlert.getNeededQuantity() + deficit);
                existingAlert.setCurrentStock(stock);
                stockAlertRepository.save(existingAlert);
            } else {
                StockAlert alert = new StockAlert();
                alert.setProduct(product);
                alert.setNeededQuantity(deficit);
                alert.setCurrentStock(stock);
                alert.setReason("ORDER_DEFICIT");
                alert.setCreatedAt(LocalDateTime.now());
                alert.setResolved(false);
                stockAlertRepository.save(alert);
            }
        }

        // Low stock alert
        if (stock < 5) {
            boolean exists = stockAlertRepository.existsByProductAndReasonAndResolvedFalse(product, "LOW_STOCK");
            if (!exists) {
                StockAlert lowStock = new StockAlert();
                lowStock.setProduct(product);
                lowStock.setNeededQuantity(0);
                lowStock.setCurrentStock(stock);
                lowStock.setReason("LOW_STOCK");
                lowStock.setCreatedAt(LocalDateTime.now());
                lowStock.setResolved(false);
                stockAlertRepository.save(lowStock);
            }
        }
    }
}
