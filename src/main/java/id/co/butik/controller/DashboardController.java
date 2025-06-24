package id.co.butik.controller;

import id.co.butik.dto.dashboard.*;
import id.co.butik.service.DataDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DataDashboardService dataDashboardService;

    public DashboardController(DataDashboardService dataDashboardService) {
        this.dataDashboardService = dataDashboardService;
    }

    @GetMapping({"/inner","/inner/"})
    public DashboardInner inner(HttpServletRequest request) {
        return dataDashboardService.getDataDashoard(request);
    }

    @GetMapping({"/product-category", "/product-category/"})
    public List<ProductCountByCategoryDto> productCategory(HttpServletRequest request) {
        return dataDashboardService.getProductByCategory(request);
    }

    @GetMapping({"/top-products", "/top-products/"})
    public List<TopProductsDto> topProducts(HttpServletRequest request, @RequestParam(required = false) Integer limit) {
        return dataDashboardService.getTopProducts(request, limit);
    }

    @GetMapping({"/recent-sales", "/recent-sales/"})
    public List<RecentSalesDto> recentSales(HttpServletRequest request, @RequestParam(required = false) Integer limit) {
        return dataDashboardService.getRecentSales(request, limit);
    }

    @GetMapping({"/monthly-sales", "/monthly-sales/"})
    public List<MonthlySalesDto> monthlySales(HttpServletRequest request) {
        return dataDashboardService.getMonthlySales(request);
    }

    @GetMapping({"/today","/today"})
    public DashboardInner today(HttpServletRequest request) {
        return dataDashboardService.getDataToday(request);
    }

    @GetMapping({"/recent-orders", "/recent-orders/"})
    public List<RecentOrderDto> recentOrders(HttpServletRequest request, @RequestParam(required = false) Integer limit) {
        return dataDashboardService.getRecentOrders(request, limit);
    }

}
