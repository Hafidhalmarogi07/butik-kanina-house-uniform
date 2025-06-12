package id.co.butik.repository;

import id.co.butik.entity.Expense;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;

public interface ExpenseRepository extends PagingAndSortingRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

    @Query("SELECT SUM(e.amount) FROM Expense e ")
    BigDecimal getTotalExpenses();
}