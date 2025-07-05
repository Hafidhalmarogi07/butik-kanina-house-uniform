package id.co.butik.controller;

import id.co.butik.dto.retur.ReturnRequest;
import id.co.butik.dto.retur.StatusUpdateRequest;
import id.co.butik.entity.OrderReturn;
import id.co.butik.service.ReturnService;
import id.co.butik.service.SaleReturnService;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/order-returns")
public class OrderReturnController {

    private final SaleReturnService saleReturnService;
    private final ReturnService returnService;

    public OrderReturnController(SaleReturnService saleReturnService, ReturnService returnService) {
        this.saleReturnService = saleReturnService;
        this.returnService = returnService;
    }

    @GetMapping({"", "/"})
    public Page<OrderReturn> getAllSaleReturns(@RequestParam Map<String, String> params) {
        PageableSpec<OrderReturn> pageableSpec = SpecificationUtils.of(params);
        return returnService.getAllReturns(pageableSpec.getSpecification(), pageableSpec.getPageable());
    }

    @GetMapping({"/{id}", "/{id}/"})
    public OrderReturn getSaleReturnById(@PathVariable Long id) {
        return returnService.getById(id);
    }

    @PostMapping({"", "/"})
    public OrderReturn createSaleReturn(@RequestBody ReturnRequest request, HttpServletRequest servletRequest) {
        return returnService.processReturn(request, servletRequest);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public OrderReturn updateSaleReturn(@PathVariable Long id, @RequestBody OrderReturn orderReturn) {
        return saleReturnService.updateSaleReturn(id, orderReturn);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public String deleteSaleReturn(@PathVariable Long id) {
        return saleReturnService.deleteSaleReturn(id);
    }

    @PatchMapping({"/{id}/status", "/{id}/status/"})
    public OrderReturn updateOrderReturnStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        return saleReturnService.updateOrderReturnStatus(id, request.getStatus());
    }
}
