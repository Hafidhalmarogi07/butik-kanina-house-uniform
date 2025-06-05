package id.co.butik.controller;

import id.co.butik.entity.Sale;
import id.co.butik.service.SaleService;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN_TOKO"})
    @GetMapping({"", "/"})
    public Page<Sale> getAllSale(@RequestParam Map<String, String> params) {
        PageableSpec<Sale> pageableSpec = SpecificationUtils.of(params);
        return saleService.getSales(pageableSpec.getSpecification(), pageableSpec.getPageable());
    }

    @Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN_TOKO"})
    @GetMapping({"/{id}", "/{id}/"})
    public Sale getSaleById(@PathVariable Long id) {
        return saleService.getSaleById(id);
    }

    @Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN_TOKO"})
    @PostMapping({"", "/"})
    public Sale createSale(@RequestBody Sale sale, HttpServletRequest servletRequest) {
        return saleService.createSale(sale, servletRequest);
    }

    @Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN_TOKO"})
    @PutMapping({"/{id}", "/{id}/"})
    public Sale updateSale(@PathVariable Long id, @RequestBody Sale sale) {
        return saleService.updateSale(id, sale);
    }

    @Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN_TOKO"})
    @DeleteMapping({"/{id}", "/{id}/"})
    public String deleteSale(@PathVariable Long id) {
        return saleService.deleteSale(id);
    }
}
