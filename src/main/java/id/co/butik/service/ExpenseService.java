package id.co.butik.service;

import id.co.butik.entity.Expense;
import id.co.butik.entity.users.UserProfile;
import id.co.butik.repository.ExpenseRepository;
import id.co.butik.repository.UserProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@Service
public class ExpenseService {
    private final ExpenseRepository repo;

    private final UserProfileRepository userProfileRepository;
    public ExpenseService(ExpenseRepository repo, UserProfileRepository userProfileRepository) {
        this.repo = repo;
        this.userProfileRepository = userProfileRepository;
    }

    public Page<Expense> findAll(Specification<Expense> spec, Pageable pageable) {
        return repo.findAll(spec, pageable);
    }
    public Expense getExpenseById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Expense with id not found"));
    }

    public String deleteExpense(Long id) {
        Optional<Expense> expense = repo.findById(id);
        if (expense.isPresent()) {
            repo.deleteById(id);
            return "{\"success\":true}";
        } else {
            return "{\"success\":false}";
        }
    }

    public Expense createExpense(Expense expense, HttpServletRequest request) {
        UserProfile userProfile = userProfileRepository.findFirstByEmail(request.getUserPrincipal().getName());
        expense.setCreatedBy(userProfile);
        return repo.save(expense);
    }

    public Expense updateExpense(Long id, Expense expense) {
        Expense expenseToUpdate = repo.findById(id).orElseThrow(() -> new RuntimeException("Expense with id not found"));
        expenseToUpdate.setCreatedBy(expense.getCreatedBy());
        expenseToUpdate.setAmount(expense.getAmount());
        expenseToUpdate.setDescription(expense.getDescription());
        expenseToUpdate.setType(expense.getType());
        expenseToUpdate.setPaymentMethod(expense.getPaymentMethod());
        expenseToUpdate.setNote(expense.getNote());
        return repo.save(expenseToUpdate);
    }
}