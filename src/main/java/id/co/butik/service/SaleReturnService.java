package id.co.butik.service;

import id.co.butik.entity.Expense;
import id.co.butik.entity.OrderReturn;
import id.co.butik.entity.Product;
import id.co.butik.enums.ExpenseType;
import id.co.butik.enums.PaymentMethod;
import id.co.butik.enums.ReturnStatus;
import id.co.butik.enums.ReturnType;
import id.co.butik.repository.ExpenseRepository;
import id.co.butik.repository.ProductRepository;
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

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ProductRepository productRepository;

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

    public OrderReturn updateOrderReturnStatus(Long id, ReturnStatus status) {
        OrderReturn orderReturn = getSaleReturnById(id);
        orderReturn.setStatusReturn(status);

        // If returnType is REFUND and status is being updated to APPROVED
        if (orderReturn.getReturnType() == ReturnType.REFUND && status == ReturnStatus.COMPLETED) {
            // Add to expense
            Expense expense = new Expense();
            expense.setDate(java.time.LocalDate.now());
            expense.setDescription("Refund for order return #" + orderReturn.getId());
            expense.setAmount(orderReturn.getTotalRefund());
            expense.setType(ExpenseType.REFUND);
            expense.setPaymentMethod(PaymentMethod.CASH);
            expense.setNote("Automatic refund for order return #" + orderReturn.getId());

            // Save the expense directly using the repository
            expenseRepository.save(expense);

            // Increase product stock for each returned product
            if (orderReturn.getDetails() != null) {
                for (id.co.butik.entity.ReturnDetail detail : orderReturn.getDetails()) {
                    Product product = detail.getProduct();
                    if (product != null) {
                        // Increase stock by the returned quantity
                        product.setStock(product.getStock() + detail.getQuantity());
                        productRepository.save(product);
                    }
                }
            }
        }

        return saleReturnRepository.save(orderReturn);
    }
}
