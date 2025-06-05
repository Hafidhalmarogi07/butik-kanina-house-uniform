package id.co.butik.controller;

import id.co.butik.entity.Customer;
import id.co.butik.service.CustomerService;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping({"", "/"})
    public Page<Customer> getAllCustomers(@RequestParam Map<String, String> params) {
        PageableSpec<Customer> pageableSpec = SpecificationUtils.of(params);
        return customerService.getCustomers(pageableSpec.getSpecification(), pageableSpec.getPageable());
    }

    @GetMapping({"/{id}", "/{id}/"})
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping({"", "/"})
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public String deleteCustomer(@PathVariable Long id) {
        return customerService.deleteCustomer(id);
    }
}
