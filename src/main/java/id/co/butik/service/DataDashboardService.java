package id.co.butik.service;

import id.co.butik.dto.dashboard.*;
import id.co.butik.entity.Order;
import id.co.butik.entity.OrderDetail;
import id.co.butik.entity.Product;
import id.co.butik.entity.Production;
import id.co.butik.entity.Sale;
import id.co.butik.entity.SaleDetail;
import id.co.butik.entity.StockAlert;
import id.co.butik.enums.OrderStatus;
import id.co.butik.enums.ProductionStatus;
import id.co.butik.repository.ExpenseRepository;
import id.co.butik.repository.OrderRepository;
import id.co.butik.repository.ProductRepository;
import id.co.butik.repository.ProductionRepository;
import id.co.butik.repository.SaleRepository;
import id.co.butik.repository.StockAlertRepository;
import id.co.butik.util.PropertiesUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DataDashboardService {


    private final SaleRepository saleRepository;
    private final OrderRepository orderRepository;
    private final ExpenseRepository expenseRepository;
    private final ProductRepository productRepository;
    private final ProductionRepository productionRepository;
    private final StockAlertRepository stockAlertRepository;

    public DataDashboardService(SaleRepository saleRepository, OrderRepository orderRepository, 
                               ExpenseRepository expenseRepository, ProductRepository productRepository,
                               ProductionRepository productionRepository, StockAlertRepository stockAlertRepository) {
        this.saleRepository = saleRepository;
        this.orderRepository = orderRepository;
        this.expenseRepository = expenseRepository;
        this.productRepository = productRepository;
        this.productionRepository = productionRepository;
        this.stockAlertRepository = stockAlertRepository;
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

    public List<MonthlySalesDto> getMonthlySales(HttpServletRequest request) {
        // Fetch monthly sales data from repository
        List<Object[]> monthlySalesData = saleRepository.getMonthlySalesForCurrentYear();

        // Convert to DTOs
        List<MonthlySalesDto> monthlySalesDtos = new ArrayList<>();

        for (Object[] data : monthlySalesData) {
            Integer monthNumber = (Integer) data[0];
            Long amount = (Long) data[1];

            // Convert month number to month name
            String monthName = Month.of(monthNumber).toString();

            MonthlySalesDto dto = new MonthlySalesDto();
            dto.setMonth(monthName);
            dto.setItems(amount);

            monthlySalesDtos.add(dto);
        }

        return monthlySalesDtos;
    }

    public DashboardInner getDataToday(HttpServletRequest request) {
        DashboardInner dashboardInner = new DashboardInner();
        dashboardInner.setSales(saleRepository.countSalesToday());
        dashboardInner.setOrders(orderRepository.countOrderToday());
        dashboardInner.setOrdersPending(orderRepository.countOrderTodayAndOrderStatus(OrderStatus.PENDING));

        // Handle potential null values from repository methods
        BigDecimal totalSales = saleRepository.getTotalSalesToday();
        BigDecimal totalOrder = orderRepository.getTotalOrderToday();

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
        return dashboardInner;

    }

    public List<RecentOrderDto> getRecentOrders(HttpServletRequest request, Integer limit) {
        int saleLimit = (limit != null && limit > 0) ? limit : 5;
        List<Order> recentOrders = orderRepository.findRecentOrder(PageRequest.of(0, saleLimit));
        List<RecentOrderDto> recentOrderDtos = new ArrayList<>();

        for (Order order : recentOrders) {
            RecentOrderDto dto = new RecentOrderDto();
            dto.setId(order.getId());
            dto.setOrderNumber(order.getOrderNumber());
            dto.setStatus(order.getOrderStatus());
            dto.setCustomerName(order.getCustomer().getNama());
            dto.setAmount(order.getTotalAmount());
            recentOrderDtos.add(dto);
        }
        return recentOrderDtos;
    }

    public List<DailySalesDto> getDailySales(HttpServletRequest request) {
        // Fetch daily sales data for the last 7 days
        List<Object[]> dailySalesData = saleRepository.getDailySalesForLastSevenDays();

        // Convert to DTOs
        List<DailySalesDto> dailySalesDtos = new ArrayList<>();

        for (Object[] data : dailySalesData) {
            LocalDate day =((java.sql.Date) data[0]).toLocalDate();
            Long amount = (Long) data[1];

            // Get day name (e.g., Monday, Tuesday, etc.)
            String dayName = day.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            DailySalesDto dto = new DailySalesDto();
            dto.setDay(day);
            dto.setDayName(dayName);
            dto.setItems(amount != null ? amount : 0);

            dailySalesDtos.add(dto);
        }

        return dailySalesDtos;
    }

    // Warehouse Dashboard Methods

    /**
     * Get warehouse summary data
     */
    public WarehouseSummaryDto getWarehouseSummary(HttpServletRequest request) {
        WarehouseSummaryDto summary = new WarehouseSummaryDto();

        // Count total inventory (sum of all product stocks)
        long totalInventory = StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .mapToLong(Product::getStock)
                .sum();
        summary.setTotalInventory(totalInventory);

        // Count incoming stock (estimate based on current productions)
        long incomingStock = StreamSupport.stream(productionRepository.findAll().spliterator(), false)
                .filter(p -> p.getStatus() == ProductionStatus.IN_PROGRESS)
                .count();
        summary.setIncomingStock(incomingStock);

        // Count outgoing stock (orders processed today)
        long outgoingStock = orderRepository.countOrderToday();
        summary.setOutgoingStock(outgoingStock);

        // Count low stock items
        long lowStockItems = stockAlertRepository.findByResolvedFalse().size();
        summary.setLowStockItems(lowStockItems);

        return summary;
    }

    /**
     * Get inventory movement data for chart
     * 
     * Retrieves stock movement data for the last 7 months:
     * - Stock In: total quantity from production table
     * - Stock Out: total quantity from sales + total quantity from completed orders
     * 
     * Returns data ordered from oldest to newest month.
     */
    public List<InventoryMovementDto> getInventoryMovement(HttpServletRequest request) {
        List<InventoryMovementDto> movementData = new ArrayList<>();

        // Get last 7 months
        LocalDate now = LocalDate.now();

        // Process months from oldest to newest (as required)
        for (int i = 6; i >= 0; i--) {
            LocalDate date = now.minusMonths(i);
            LocalDate startOfMonth = date.withDayOfMonth(1);
            LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());

            String monthName = date.getMonth().getDisplayName(TextStyle.FULL, new Locale("id", "ID"));

            // Calculate stock in (total quantity from production table)
            long incomingStock = StreamSupport.stream(productionRepository.findAll().spliterator(), false)
                    .filter(p -> {
                        LocalDate productionDate = p.getEndDate() != null ? p.getEndDate() : p.getStartDate();
                        return productionDate != null && 
                               !productionDate.isBefore(startOfMonth) && 
                               !productionDate.isAfter(endOfMonth);
                    })
                    .mapToInt(Production::getQuantity)
                    .sum();

            // Calculate stock out from sales
            long outgoingStockFromSales = StreamSupport.stream(saleRepository.findAll().spliterator(), false)
                    .filter(s -> {
                        LocalDate saleDate = s.getDate() != null ? s.getDate().toLocalDate() : null;
                        return saleDate != null && 
                               !saleDate.isBefore(startOfMonth) && 
                               !saleDate.isAfter(endOfMonth);
                    })
                    .mapToInt(s -> s.getItems() != null ? s.getItems() : 0)
                    .sum();

            // Calculate stock out from completed orders
            long outgoingStockFromOrders = StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                    .filter(o -> {
                        LocalDate orderDate = o.getOrderDate() != null ? o.getOrderDate().toLocalDate() : null;
                        return orderDate != null && 
                               !orderDate.isBefore(startOfMonth) && 
                               !orderDate.isAfter(endOfMonth) &&
                               o.getOrderStatus() == OrderStatus.COMPLETED;
                    })
                    .flatMap(o -> o.getDetails().stream())
                    .mapToInt(od -> od.getQuantity())
                    .sum();

            // Total outgoing stock
            long outgoingStock = outgoingStockFromSales + outgoingStockFromOrders;

            // Add to result list
            movementData.add(new InventoryMovementDto(monthName, incomingStock, outgoingStock));
        }

        // Reverse the list to get oldest to newest order
        List<InventoryMovementDto> orderedData = new ArrayList<>();
        for (int i = movementData.size() - 1; i >= 0; i--) {
            orderedData.add(movementData.get(i));
        }

        return orderedData;
    }

    /**
     * Get warehouse capacity data
     */
    public WarehouseCapacityDto getWarehouseCapacity(HttpServletRequest request) {
        // For demonstration, we'll assume a fixed warehouse capacity of 1000 items
        // In a real application, this would come from a configuration or database
        long totalCapacity = 50000;

        // Calculate used space (sum of all product stocks)
        long usedSpace = StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .mapToLong(Product::getStock)
                .sum();

        // Calculate available space
        long availableSpace = Math.max(0, totalCapacity - usedSpace);

        return new WarehouseCapacityDto(usedSpace, availableSpace);
    }

    /**
     * Get recent stock movements from production, sales, and orders
     * 
     * @param request HTTP request
     * @param limit Maximum number of movements to return (default 6)
     * @return List of recent stock movements
     */
    public List<StockMovementDto> getRecentStockMovements(HttpServletRequest request, Integer limit) {
        int movementLimit = (limit != null && limit > 0) ? limit : 6;
        List<StockMovementDto> movements = new ArrayList<>();

        // 1. Get recent productions (stock increases)
        List<Production> recentProductions = StreamSupport.stream(productionRepository.findAll().spliterator(), false)
                .filter(p -> p.getStatus() == ProductionStatus.FINISHED)
                .sorted((a, b) -> {
                    LocalDate dateA = a.getEndDate() != null ? a.getEndDate() : a.getStartDate();
                    LocalDate dateB = b.getEndDate() != null ? b.getEndDate() : b.getStartDate();
                    return dateB.compareTo(dateA); // Sort by date descending
                })
                .limit(movementLimit)
                .collect(Collectors.toList());

        for (Production production : recentProductions) {
            if (production.getProduct() != null) {
                LocalDate date = production.getEndDate() != null ? production.getEndDate() : production.getStartDate();
                StockMovementDto dto = new StockMovementDto(
                    "P" + production.getId(),
                    production.getProduct().getName(),
                    "IN",
                    production.getQuantity(),
                    date,
                    "Produksi",
                    "Production"
                );
                movements.add(dto);
            }
        }

        // 2. Get recent sales (stock decreases)
        List<Sale> recentSales = saleRepository.findRecentSales(PageRequest.of(0, movementLimit));

        for (Sale sale : recentSales) {
            if (sale.getDetails() != null && !sale.getDetails().isEmpty() && sale.getDate() != null) {
                for (SaleDetail detail : sale.getDetails()) {
                    if (detail.getProduct() != null) {
                        StockMovementDto dto = new StockMovementDto(
                            "S" + sale.getId() + "-" + detail.getId(),
                            detail.getProduct().getName(),
                            "OUT",
                            -detail.getQuantity(), // Negative quantity for stock decrease
                            sale.getDate().toLocalDate(),
                            "Penjualan",
                            "Sale"
                        );
                        movements.add(dto);
                    }
                }
            }
        }

        // 3. Get recent completed orders (stock decreases)
        List<Order> recentOrders = orderRepository.findRecentOrder(PageRequest.of(0, movementLimit));

        for (Order order : recentOrders) {
            if (order.getOrderStatus() == OrderStatus.COMPLETED && order.getOrderDate() != null) {
                // For simplicity, we'll just create one movement per order
                // In a real implementation, you might want to iterate through order details
                StockMovementDto dto = new StockMovementDto(
                    "O" + order.getId(),
                    "Order #" + order.getOrderNumber(),
                    "OUT",
                    -1, // Just indicate it's a decrease
                    order.getOrderDate().toLocalDate(),
                    "Order keluar",
                    "Order"
                );
                movements.add(dto);
            }
        }

        // Sort by date (most recent first) and limit
        movements.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        if (movements.size() > movementLimit) {
            movements = movements.subList(0, movementLimit);
        }

        return movements;
    }

    /**
     * Get low stock items
     */
    public List<LowStockItemDto> getLowStockItems(HttpServletRequest request, Integer limit) {
        int itemLimit = (limit != null && limit > 0) ? limit : 5;
        List<LowStockItemDto> lowStockItems = new ArrayList<>();

        // Get unresolved stock alerts
        List<StockAlert> stockAlerts = stockAlertRepository.findByResolvedFalse();

        for (StockAlert alert : stockAlerts) {
            Product product = alert.getProduct();
            if (product != null) {
                LowStockItemDto dto = new LowStockItemDto(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getImageUrl() != null ? PropertiesUtils.CDN_BASEURL + product.getImageUrl() : null,
                    alert.getCurrentStock(),
                    product.getSellingPrice()
                );
                lowStockItems.add(dto);
            }
        }

        // If we don't have enough items from stock alerts, add products with low stock
        if (lowStockItems.size() < itemLimit) {
            // Get all products sorted by stock (ascending)
            List<Product> products = StreamSupport.stream(productRepository.findAll().spliterator(), false)
                    .sorted((a, b) -> Integer.compare(a.getStock(), b.getStock()))
                    .collect(Collectors.toList());

            for (Product product : products) {
                // Skip products that are already in the list
                if (lowStockItems.stream().anyMatch(item -> item.getProductId().equals(product.getId()))) {
                    continue;
                }

                // Add products with stock less than 10 (arbitrary threshold)
                if (product.getStock() < 10) {
                    LowStockItemDto dto = new LowStockItemDto(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getImageUrl() != null ? PropertiesUtils.CDN_BASEURL + product.getImageUrl() : null,
                        product.getStock(),
                        product.getSellingPrice()
                    );
                    lowStockItems.add(dto);

                    // Stop if we have enough items
                    if (lowStockItems.size() >= itemLimit) {
                        break;
                    }
                }
            }
        }

        // Limit the results
        if (lowStockItems.size() > itemLimit) {
            lowStockItems = lowStockItems.subList(0, itemLimit);
        }

        return lowStockItems;
    }
}
