package id.co.butik.controller;

import id.co.butik.entity.Production;
import id.co.butik.service.ProductionService;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/productions")
public class ProductionController {

    @Autowired
    private ProductionService productionService;

    @RequestMapping({"", "/"})
    public Page<Production> getAllProductions(@RequestParam Map<String, String> params) {
        PageableSpec<Production> pageableSpec = SpecificationUtils.of(params);
        return productionService.findAll(pageableSpec.getSpecification(), pageableSpec.getPageable());
    }

    @RequestMapping({"/{id}", "/{id}/"})
    public Production getProductionById(@PathVariable Long id) {
        return productionService.findById(id);
    }

    @PostMapping({"", "/"})
    public Production createProduction(@RequestBody Production production) {
        return productionService.createProduction(production);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public Production updateProduction(@PathVariable Long id, @RequestBody Production production) {
        return productionService.updateProduction(id, production);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public String deleteProduction(@PathVariable Long id) {
        return productionService.deleteProduction(id);
    }
}
