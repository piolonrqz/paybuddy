package com.splitwise.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.splitwise.Entities.Expense;
import com.splitwise.Entities.ExpenseShare;
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>{
    List<Expense> findByGroupById(Long groupId);
    List<ExpenseShare> findByExpenseId(Long expenseId);

}
