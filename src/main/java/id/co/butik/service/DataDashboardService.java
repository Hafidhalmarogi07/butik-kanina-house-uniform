package id.co.butik.service;

import id.co.butik.dto.dashboard.DashboardInner;
import id.co.butik.dto.dashboard.ProductCountByCategoryDto;
import id.co.butik.dto.dashboard.RecentSalesDto;
import id.co.butik.dto.dashboard.TopProductsDto;
import id.co.butik.entity.Sale;
import id.co.butik.entity.SaleDetail;
import id.co.butik.repository.ExpenseRepository;
import id.co.butik.repository.OrderRepository;
import id.co.butik.repository.ProductRepository;
import id.co.butik.repository.SaleRepository;
import id.co.butik.util.PropertiesUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataDashboardService {


    private final SaleRepository saleRepository;
    private final OrderRepository orderRepository;
    private final ExpenseRepository expenseRepository;
    private final ProductRepository productRepository;

    public DataDashboardService(SaleRepository saleRepository, OrderRepository orderRepository, ExpenseRepository expenseRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.orderRepository = orderRepository;
        this.expenseRepository = expenseRepository;
        this.productRepository = productRepository;
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

    public List<ProductCountByCategoryDto> getProductByCategory(HttpServletRequest request) {
        return productRepository.countProductsByCategory();
    }

    public List<TopProductsDto> getTopProducts(HttpServletRequest request, Integer limit) {
        List<TopProductsDto> topProducts = productRepository.findTopProducts();

        // Apply CDN base URL to image URLs
        topProducts = topProducts.stream()
                .peek(product -> {
                    if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                        product.setImageUrl(PropertiesUtils.CDN_BASEURL + product.getImageUrl());
                    }
                })
                .collect(Collectors.toList());

        // Limit the results if a limit is provided
        if (limit != null && limit > 0 && topProducts.size() > limit) {
            return topProducts.subList(0, limit);
        }

        return topProducts;
    }

    public List<RecentSalesDto> getRecentSales(HttpServletRequest request, Integer limit) {
        // Default limit to 5 if not provided
        int saleLimit = (limit != null && limit > 0) ? limit : 5;

        // Fetch recent sales from repository
        List<Sale> recentSales = saleRepository.findRecentSales(PageRequest.of(0, saleLimit));

        // Convert to DTOs
        List<RecentSalesDto> recentSalesDtos = new ArrayList<>();

        for (Sale sale : recentSales) {
            // For each sale, we need to extract the item name from the first sale detail
            String itemName = "Unknown Item";
            if (sale.getDetails() != null && !sale.getDetails().isEmpty()) {
                SaleDetail firstDetail = sale.getDetails().get(0);
                if (firstDetail.getProduct() != null) {
                    itemName = firstDetail.getProduct().getName();
                }
            }

            RecentSalesDto dto = new RecentSalesDto();
            dto.setId(sale.getId());
            dto.setInvoiceNumber(sale.getInvoiceNumber());
            dto.setItemName(itemName);
            dto.setStatus(sale.getStatus());
            dto.setAmount(sale.getTotal());

            recentSalesDtos.add(dto);
        }

        return recentSalesDtos;
    }
}
