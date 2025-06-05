package id.co.butik.controller;

import id.co.butik.entity.SaleReturn;
import id.co.butik.service.SaleReturnService;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/sale-returns")
public class SaleReturnController {

    private final SaleReturnService saleReturnService;

    public SaleReturnController(SaleReturnService saleReturnService) {
        this.saleReturnService = saleReturnService;
    }

    @GetMapping({"", "/"})
    public Page<SaleReturn> getAllSaleReturns(@RequestParam Map<String, String> params) {
        PageableSpec<SaleReturn> pageableSpec = SpecificationUtils.of(params);
        return saleReturnService.getSaleReturns(pageableSpec.getSpecification(), pageableSpec.getPageable());
    }

    @GetMapping({"/{id}", "/{id}/"})
    public SaleReturn getSaleReturnById(@PathVariable Long id) {
        return saleReturnService.getSaleReturnById(id);
    }

    @PostMapping({"", "/"})
    public SaleReturn createSaleReturn(@RequestBody SaleReturn saleReturn) {
        return saleReturnService.createSaleReturn(saleReturn);
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
