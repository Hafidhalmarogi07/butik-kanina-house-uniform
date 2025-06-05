package id.co.butik.controller;

import id.co.butik.entity.SaleDetail;
import id.co.butik.service.SaleDetailService;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/sale-details")
public class SaleDetailController {

    private final SaleDetailService saleDetailService;

    public SaleDetailController(SaleDetailService saleDetailService) {
        this.saleDetailService = saleDetailService;
    }

    @GetMapping({"", "/"})
    public Page<SaleDetail> getAllSaleDetails(@RequestParam Map<String, String> params) {
        PageableSpec<SaleDetail> pageableSpec = SpecificationUtils.of(params);
        return saleDetailService.getSaleDetails(pageableSpec.getSpecification(), pageableSpec.getPageable());
    }

    @GetMapping({"/{id}", "/{id}/"})
    public SaleDetail getSaleDetailById(@PathVariable Long id) {
        return saleDetailService.getSaleDetailById(id);
    }

    @PostMapping({"", "/"})
    public SaleDetail createSaleDetail(@RequestBody SaleDetail saleDetail) {
        return saleDetailService.createSaleDetail(saleDetail);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public SaleDetail updateSaleDetail(@PathVariable Long id, @RequestBody SaleDetail saleDetail) {
        return saleDetailService.updateSaleDetail(id, saleDetail);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public String deleteSaleDetail(@PathVariable Long id) {
        return saleDetailService.deleteSaleDetail(id);
    }
}
