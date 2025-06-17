package id.co.butik.repository;

import id.co.butik.entity.Expense;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseReportRepository extends PagingAndSortingRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

    /**
     * Find expenses within a date range
     * @param startDate the start date
     * @param endDate the end date
     * @return list of expenses within the date range
     */
    @Query("SELECT e FROM Expense e WHERE e.date >= :startDate AND e.date <= :endDate ORDER BY e.date")
    List<Expense> findExpensesByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}