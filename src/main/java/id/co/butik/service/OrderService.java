package id.co.butik.service;

import id.co.butik.dto.order.OrderDetailRequest;
import id.co.butik.dto.order.OrderPaymentRequest;
import id.co.butik.dto.order.OrderRequest;
import id.co.butik.entity.*;
import id.co.butik.entity.users.UserProfile;
import id.co.butik.enums.OrderStatus;
import id.co.butik.enums.PaymentStatus;
import id.co.butik.repository.*;
import id.co.butik.util.NumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;
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
    private NumberGenerator numberGenerator;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository,
                        ProductRepository productRepository, OrderDetailRepository orderDetailRepository,
                        OrderPaymentRepository orderPaymentRepository, UserProfileRepository userProfileRepository, NumberGenerator numberGenerator) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderPaymentRepository = orderPaymentRepository;
        this.userProfileRepository = userProfileRepository;
        this.numberGenerator = numberGenerator;
    }


    public Page<Order> findAll(Specification<Order> spec, Pageable pageable) {
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

//            if (product.getStock() < detailReq.getQuantity()) {
//                throw new RuntimeException("Stock tidak mencukupi untuk produk: " + product.getName());
//            }
//
//            product.setStock(product.getStock() - detailReq.getQuantity());
//            productRepository.save(product);

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(detailReq.getQuantity());
            detail.setPrice(detailReq.getUnitPrice());
            detail.setSubtotal(product.getSellingPrice().multiply(BigDecimal.valueOf(detailReq.getQuantity())));
            detailList.add(detail);
            totalAmount = totalAmount.add(product.getSellingPrice().multiply(BigDecimal.valueOf(detailReq.getQuantity())));
        }

        order.setTotalAmount(totalAmount);
        order.setAmountPaid(BigDecimal.ZERO); // akan dihitung dari payments

       // Order savedOrder = orderRepository.save(order);
       // orderDetailRepository.saveAll(detailList);
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

                totalPaid = totalPaid.add(payment.getAmount());
                payments.add(payment);
            }
            order.setPayments(payments);
            //orderPaymentRepository.saveAll(payments);
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

}
