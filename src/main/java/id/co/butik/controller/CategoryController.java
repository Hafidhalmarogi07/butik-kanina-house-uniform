package id.co.butik.controller;

import id.co.butik.dto.PageResponse;
import id.co.butik.entity.Category;
import id.co.butik.service.CategoryService;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping({"", "/"})
    public PageResponse<Category> getAllCategory(@RequestParam Map<String, String> params) {
        PageableSpec<Category> pageableSpec = SpecificationUtils.of(params);
        Page<Category> page = categoryService.getCategories(pageableSpec.getSpecification(), pageableSpec.getPageable());
        return PageResponse.from(page);
    }

    @GetMapping({"/{id}", "/{id}/"})
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping({"", "/"})
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public String deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }


}
