package id.co.butik.controller;

import id.co.butik.dto.PageResponse;
import id.co.butik.entity.Category;
import id.co.butik.service.CategoryService;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

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
    public Category createCategory( @Valid @RequestBody Category category) {
        return categoryService.createCategory(category);
    }


    @PutMapping({"/{id}", "/{id}/"})
    public Category updateCategory(@PathVariable Long id,  @Valid  @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public String deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }
}
