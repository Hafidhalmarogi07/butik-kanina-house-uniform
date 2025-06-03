package id.co.butik.controller;

import id.co.butik.entity.Sale;
import id.co.butik.service.SaleService;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping({"", "/"})
    public Page<Sale> getAllSale(@RequestParam Map<String, String> params) {
        PageableSpec<Sale> pageableSpec = SpecificationUtils.of(params);
        return saleService.getSales(pageableSpec.getSpecification(), pageableSpec.getPageable());
    }

    @GetMapping({"/{id}", "/{id}/"})
    public Sale getSaleById(@PathVariable Long id) {
        return saleService.getSaleById(id);
    }

    @PostMapping({"", "/"})
    public Sale createSale(@RequestBody Sale sale) {
        return saleService.createSale(sale);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public Sale updateSale(@PathVariable Long id, @RequestBody Sale sale) {
        return saleService.updateSale(id, sale);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public String deleteSale(@PathVariable Long id) {
        return saleService.deleteSale(id);
    }
}
