package id.co.butik.controller;

import id.co.butik.dto.order.OrderPaymentRequest;
import id.co.butik.dto.order.OrderRequest;
import id.co.butik.dto.order.OrderStatusUpdateRequest;
import id.co.butik.entity.Order;
import id.co.butik.service.OrderService;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    // ✅ Create new Order (dengan detail dan pembayaran awal)
    @Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN_TOKO"})
    @PostMapping({"","/"})
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request, HttpServletRequest httpServletRequest) {
        Order created = orderService.createOrder(request, httpServletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ✅ Get all orders
    @GetMapping
    public Page<Order> getAllOrders(@RequestParam Map<String, String> params) {
        PageableSpec<Order> pageableSpec = SpecificationUtils.of(params);
        return orderService.findAll(pageableSpec.getSpecification(), pageableSpec.getPageable());
    }

    // ✅ Get order by id
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Tambah pembayaran (pelunasan atau cicilan)
    @PostMapping("/{id}/payment")
    public ResponseEntity<Order> addPayment(
            @PathVariable Long id,
            @RequestBody OrderPaymentRequest paymentRequest
    ) {
        Order updated = orderService.addPayment(id, paymentRequest);
        return ResponseEntity.ok(updated);
    }

    // ✅ Batalkan pesanan (opsional)
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        Order canceled = orderService.cancelOrder(id);
        return ResponseEntity.ok(canceled);
    }

    // ✅ Update order status
    @PostMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(
            @PathVariable Long id,
            @RequestBody OrderStatusUpdateRequest request
    ) {
        Order updated = orderService.updateOrderStatus(id, request.getStatus());
        return ResponseEntity.ok(updated);
    }

    // ✅ Update an order
    @Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN_TOKO"})
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody OrderRequest request) {
        Order updated = orderService.updateOrder(id, request);
        return ResponseEntity.ok(updated);
    }

    // ✅ Delete an order
    @Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN_TOKO"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
