package id.co.butik.service;

import id.co.butik.entity.Customer;
import id.co.butik.repository.CustomerRepository;
import id.co.butik.responseException.BadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Page<Customer> getCustomers(Specification<Customer> var1, Pageable var2) {
        return customerRepository.findAll(var1, var2);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new BadRequest("Customer with id not found "));
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customer) {
        Customer oldCustomer = getCustomerById(id);
        oldCustomer.setNama(customer.getNama());
        oldCustomer.setAlamat(customer.getAlamat());
        oldCustomer.setPhoneNumber(customer.getPhoneNumber());
        oldCustomer.setTipe(customer.getTipe());
        return customerRepository.save(oldCustomer);
    }

    public String deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new BadRequest("Customer not found "));
        customerRepository.deleteById(id);

        return "{\"success\":true}";
    }

}
