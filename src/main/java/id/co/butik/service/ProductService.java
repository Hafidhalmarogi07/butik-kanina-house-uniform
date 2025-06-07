package id.co.butik.service;

import id.co.butik.entity.Category;
import id.co.butik.entity.Product;
import id.co.butik.repository.CategoryRepository;
import id.co.butik.repository.ProductRepository;
import id.co.butik.responseException.BadRequest;
import id.co.butik.util.ImageUtils;
import id.co.butik.util.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Product> getProducts(Specification<Product> var1, Pageable var2) {
        Page<Product> products = productRepository.findAll(var1, var2);
        for (Product product : products){
            product.setImageUrl(PropertiesUtils.CDN_BASEURL+product.getImageUrl());
        }
        return products;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new BadRequest("Product with id not found"));
    }

    public Product createProduct(Product product) {
        log.info("Create new product : {}", product);
        Category category = categoryRepository.findById(product.getCategory().getId()).orElseThrow(() -> new BadRequest("Category with id not found"));
        log.info("get category : {}", category);
        product.setCategory(category);
        if(product.getImageUrl().isEmpty()) throw new BadRequest("Gambar produk dibutuhkan");
        product.setImageUrl(ImageUtils.fromBase64(product.getImageUrl(), PropertiesUtils.CDN_PATH+"/product", "/product"));
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        Product oldProduct = getProductById(id);
        oldProduct.setName(product.getName());
        oldProduct.setDescription(product.getDescription());
        oldProduct.setStock(product.getStock());
        oldProduct.setSize(product.getSize());
        oldProduct.setSellingPrice(product.getSellingPrice());


        Category category = categoryRepository.findById(product.getCategory().getId()).orElseThrow(() -> new BadRequest("Category with id not found"));
        oldProduct.setCategory(category);
        if(product.getImageUrl() != null) oldProduct.setImageUrl(ImageUtils.fromBase64(product.getImageUrl(), PropertiesUtils.CDN_PATH+"/product", "/product"));
        String[] linkImage = oldProduct.getImageUrl().split("cdn");
        if(linkImage.length == 2){
            oldProduct.setImageUrl(linkImage[1]);
        }
        return productRepository.save(oldProduct);
    }

    public String deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.deleteById(id);

        return "{\"success\":true}";
    }
}
