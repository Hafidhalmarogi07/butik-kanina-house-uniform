package id.co.butik.service;

import id.co.butik.dto.DashboardInner;
import id.co.butik.repository.ExpenseRepository;
import id.co.butik.repository.OrderRepository;
import id.co.butik.repository.SaleRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Service
public class DataDashboardService {


    private final SaleRepository saleRepository;
    private final OrderRepository orderRepository;
    private final ExpenseRepository expenseRepository;

    public DataDashboardService(SaleRepository saleRepository, OrderRepository orderRepository, ExpenseRepository expenseRepository) {
        this.saleRepository = saleRepository;
        this.orderRepository = orderRepository;
        this.expenseRepository = expenseRepository;
    }

    public DashboardInner getDataDashoard(HttpServletRequest request) {
        DashboardInner dashboardInner = new DashboardInner();

        dashboardInner.setSales(saleRepository.count());
        dashboardInner.setOrders(orderRepository.count());

        // Handle potential null values from repository methods
        BigDecimal totalSales = saleRepository.getTotalSales();
        BigDecimal totalOrder = orderRepository.getTotalOrder();
        BigDecimal totalExpenses = expenseRepository.getTotalExpenses();

        // Set revenue (sum of sales and orders)
        if (totalSales == null && totalOrder == null) {
            dashboardInner.setRevenue(BigDecimal.ZERO);
        } else if (totalSales == null) {
            dashboardInner.setRevenue(totalOrder);
        } else if (totalOrder == null) {
            dashboardInner.setRevenue(totalSales);
        } else {
            dashboardInner.setRevenue(totalSales.add(totalOrder));
        }

        // Set expenses
        dashboardInner.setExpenses(totalExpenses != null ? totalExpenses : BigDecimal.ZERO);

        return dashboardInner;
    }
}
