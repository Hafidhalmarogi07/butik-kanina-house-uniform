package id.co.butik.controller;

import id.co.butik.entity.Product;
import id.co.butik.service.ProductService;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping({"", "/"})
    public Page<Product> getAllProducts(@RequestParam Map<String, String> params) {
        PageableSpec<Product> pageableSpec = SpecificationUtils.of(params);
        return productService.getProducts(pageableSpec.getSpecification(), pageableSpec.getPageable());
    }

    @GetMapping({"/{id}", "/{id}/"})
    public Product getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping({"", "/"})
    public Product addProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public String deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

}
