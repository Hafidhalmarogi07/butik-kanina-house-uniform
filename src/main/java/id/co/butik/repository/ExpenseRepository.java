package id.co.butik.repository;

import id.co.butik.entity.Expense;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ExpenseRepository extends PagingAndSortingRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {}