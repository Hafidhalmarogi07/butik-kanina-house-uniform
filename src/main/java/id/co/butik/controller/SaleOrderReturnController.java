package id.co.butik.controller;

import id.co.butik.dto.retur.ReturnRequest;
import id.co.butik.entity.SaleReturn;
import id.co.butik.service.ReturnService;
import id.co.butik.service.SaleReturnService;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sale-order-returns")
public class SaleOrderReturnController {

    private final SaleReturnService saleReturnService;
    private final ReturnService returnService;

    public SaleOrderReturnController(SaleReturnService saleReturnService, ReturnService returnService) {
        this.saleReturnService = saleReturnService;
        this.returnService = returnService;
    }

    @GetMapping({"", "/"})
    public Page<SaleReturn> getAllSaleReturns(@RequestParam Map<String, String> params) {
        PageableSpec<SaleReturn> pageableSpec = SpecificationUtils.of(params);
        return returnService.getAllReturns(pageableSpec.getSpecification(), pageableSpec.getPageable());
    }

    @GetMapping({"/{id}", "/{id}/"})
    public SaleReturn getSaleReturnById(@PathVariable Long id) {
        return returnService.getById(id);
    }

    @PostMapping({"", "/"})
    public SaleReturn createSaleReturn(@RequestBody ReturnRequest request, HttpServletRequest servletRequest) {
        return returnService.processReturn(request, servletRequest);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public SaleReturn updateSaleReturn(@PathVariable Long id, @RequestBody SaleReturn saleReturn) {
        return saleReturnService.updateSaleReturn(id, saleReturn);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public String deleteSaleReturn(@PathVariable Long id) {
        return saleReturnService.deleteSaleReturn(id);
    }
}
