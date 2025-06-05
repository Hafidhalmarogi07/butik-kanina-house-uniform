package id.co.butik.controller;

import id.co.butik.entity.ReturnDetail;
import id.co.butik.service.ReturnDetailService;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/return-details")
public class ReturnDetailController {

    private final ReturnDetailService returnDetailService;

    public ReturnDetailController(ReturnDetailService returnDetailService) {
        this.returnDetailService = returnDetailService;
    }

    @GetMapping({"", "/"})
    public Page<ReturnDetail> getAllReturnDetails(@RequestParam Map<String, String> params) {
        PageableSpec<ReturnDetail> pageableSpec = SpecificationUtils.of(params);
        return returnDetailService.findAll(pageableSpec.getSpecification(), pageableSpec.getPageable());
    }

    @GetMapping({"/{id}", "/{id}/"})
    public ReturnDetail getReturnDetailById(@PathVariable Long id) {
        return returnDetailService.findById(id);
    }

    @PostMapping({"", "/"})
    public ReturnDetail createReturnDetail(@RequestBody ReturnDetail returnDetail) {
        return returnDetailService.save(returnDetail);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public ReturnDetail updateReturnDetail(@PathVariable Long id, @RequestBody ReturnDetail returnDetail) {
        return returnDetailService.update(id, returnDetail);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public String deleteReturnDetail(@PathVariable Long id) {
        return returnDetailService.deleteById(id);
    }
}
