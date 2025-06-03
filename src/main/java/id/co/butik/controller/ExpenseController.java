package id.co.butik.controller;

import id.co.butik.entity.Expense;
import id.co.butik.service.ExpenseService;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping({"", "/"})
    public Page<Expense> getAllExpenses(@RequestParam Map<String, String> params) {
        PageableSpec<Expense> pageableSpec = SpecificationUtils.of(params);
        return expenseService.findAll(pageableSpec.getSpecification(), pageableSpec.getPageable());
    }

    @GetMapping({"/{id}", "/{id}/"})
    public Expense getExpenseById(@PathVariable Long id) {
        return expenseService.getExpenseById(id);
    }

    @PostMapping({"", "/"})
    public Expense createExpense(@RequestBody Expense expense,  HttpServletRequest request) {
        return expenseService.createExpense(expense, request);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public Expense updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        return expenseService.updateExpense(id, expense);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public String deleteExpense(@PathVariable Long id) {
        return expenseService.deleteExpense(id);
    }

}
