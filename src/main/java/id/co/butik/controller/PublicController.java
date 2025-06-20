package id.co.butik.controller;

import id.co.butik.entity.Category;
import id.co.butik.entity.Product;
import id.co.butik.service.CategoryService;
import id.co.butik.service.ProductService;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for public access to product catalog
 * This controller provides endpoints for accessing products and categories without authentication
 */
@RestController
@RequestMapping("/api/v1/public")
public class PublicController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public PublicController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    /**
     * Get all products with pagination and filtering
     * @param params Query parameters for filtering and pagination
     * @return Page of products
     */
    @GetMapping("/products")
    public Page<Product> getAllProducts(@RequestParam Map<String, String> params) {
        PageableSpec<Product> pageableSpec = SpecificationUtils.of(params);
        return productService.getProducts(pageableSpec.getSpecification(), pageableSpec.getPageable());
    }

    /**
     * Get a product by ID
     * @param id Product ID
     * @return Product
     */
    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    /**
     * Get all categories
     * @param params Query parameters for filtering and pagination
     * @return Page of categories
     */
    @GetMapping("/categories")
    public Page<Category> getAllCategories(@RequestParam Map<String, String> params) {
        PageableSpec<Category> pageableSpec = SpecificationUtils.of(params);
        return categoryService.getCategories(pageableSpec.getSpecification(), pageableSpec.getPageable());
    }

    /**
     * Get contact information
     * @return Contact information
     */
    @GetMapping("/contact")
    public Map<String, String> getContactInfo() {
        Map<String, String> contactInfo = new HashMap<>();
        contactInfo.put("email", "admin@butik-kanina.com");
        contactInfo.put("whatsapp", "+6281234567890");
        contactInfo.put("address", "Jl. Contoh No. 123, Jakarta");
        contactInfo.put("phone", "+6281234567890");
        return contactInfo;
    }
}
