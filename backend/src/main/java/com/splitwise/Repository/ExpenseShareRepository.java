package com.splitwise.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.splitwise.Entities.ExpenseShare;

@Repository
public interface ExpenseShareRepository extends JpaRepository<ExpenseShare, Long>{
    List<ExpenseShare> findByExpenseId(Long expenseId);
    List<ExpenseShare> findByUserId(Long userId);
}
