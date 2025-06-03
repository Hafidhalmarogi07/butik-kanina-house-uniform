package id.co.butik.service;

import id.co.butik.entity.Category;
import id.co.butik.repository.CategoryRepository;
import id.co.butik.responseException.BadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryService {

    @Autowired
    private  CategoryRepository categoryRepository;

    public Page<Category> getCategories(Specification<Category> var1, Pageable var2) {
        return categoryRepository.findAll(var1, var2);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new BadRequest("Category white id not found "));
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category category) {
        Category oldCategory = getCategoryById(id);
        oldCategory.setName(category.getName());
        oldCategory.setDescription(category.getDescription());
        return categoryRepository.save(oldCategory);
    }

    public String deleteCategory(Long id) {

        Category category = categoryRepository.findById(id).orElseThrow(() -> new BadRequest("Category not found "));
        categoryRepository.deleteById(id);

        return "{\"success\":true}";
    }


}
