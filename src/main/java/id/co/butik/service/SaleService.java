package id.co.butik.service;

import id.co.butik.entity.Customer;
import id.co.butik.entity.Product;
import id.co.butik.entity.Sale;
import id.co.butik.entity.SaleDetail;
import id.co.butik.entity.users.UserProfile;
import id.co.butik.enums.SaleStatus;
import id.co.butik.repository.CustomerRepository;
import id.co.butik.repository.ProductRepository;
import id.co.butik.repository.SaleRepository;
import id.co.butik.repository.UserProfileRepository;
import id.co.butik.responseException.BadRequest;
import id.co.butik.util.InvoiceNumberGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Slf4j
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private InvoiceNumberGenerator invoiceNumberGenerator;

    public Page<Sale> getSales(Specification<Sale> var1, Pageable var2) {
        return saleRepository.findAll(var1, var2);
    }

    public Sale getSaleById(Long id) {
        return saleRepository.findById(id).orElseThrow(() -> new BadRequest("Sale with id not found "));
    }

    @Transactional
    public Sale createSale(Sale sale,  HttpServletRequest servletRequest) {

        if ( null == sale ) {
            throw new BadRequest("Sale is null");
        }
        if(null == sale.getCustomer() ||  null == sale.getCustomer().getId() ) {
            throw new BadRequest("Sale customer id not found");
        }
        if(null == sale.getDetails() || sale.getDetails().isEmpty()) {
            throw new BadRequest("Sale details not found");
        }

        UserProfile userProfile = userProfileRepository.findFirstByEmail(servletRequest.getUserPrincipal().getName());
        sale.setAdmin(userProfile);

        Customer customer = customerRepository.findById(sale.getCustomer().getId()).orElseThrow(() -> new BadRequest("Customer not found"));
        sale.setCustomer(customer);

        BigDecimal total = BigDecimal.valueOf(0);

        for(SaleDetail saleDetail : sale.getDetails()) {
            if(null == saleDetail.getProduct()){
                throw new BadRequest("Product not found");
            }
            if(saleDetail.getQuantity() <= 0){
                throw new BadRequest("Quantity not found");
            }
            Product product = productRepository.findById(saleDetail.getProduct().getId()).orElseThrow(() -> new BadRequest("Product not found"));
            saleDetail.setProduct(product);
            saleDetail.getProduct().setStock(saleDetail.getProduct().getStock() - saleDetail.getQuantity());
            saleDetail.setSubtotal(product.getSellingPrice().multiply(BigDecimal.valueOf(saleDetail.getQuantity())));
            total = total.add(product.getSellingPrice().multiply(BigDecimal.valueOf(saleDetail.getQuantity())));
            saleDetail.setSale(sale);

        }
        sale.setItems(sale.getDetails().size());
        sale.setTotal(total);
        sale.setStatus(SaleStatus.COMPLETED);
        sale.setInvoiceNumber(invoiceNumberGenerator.generateInvoiceNumber());
        sale.setDate(LocalDateTime.now());
        return saleRepository.save(sale);
    }

    public Sale updateSale(Long id, Sale sale) {
        Sale oldSale = getSaleById(id);
        oldSale.setDate(sale.getDate());
        oldSale.setCustomer(sale.getCustomer());
        oldSale.setAdmin(sale.getAdmin());
        oldSale.setTotal(sale.getTotal());
        oldSale.setDetails(sale.getDetails());
        return saleRepository.save(oldSale);
    }

    public String deleteSale(Long id) {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new BadRequest("Sale not found "));
        saleRepository.deleteById(id);
        return "{\"success\":true}";
    }
}
