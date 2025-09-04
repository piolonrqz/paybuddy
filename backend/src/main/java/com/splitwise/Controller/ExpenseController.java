package com.splitwise.Controller;

import com.splitwise.DTO.ExpenseDTO;
import com.splitwise.DTO.ExpenseShareDTO;
import com.splitwise.Service.ExpenseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    //Add expense
    @PostMapping("/add")
    public ExpenseDTO addExpense(@RequestParam Long groupId,
                                 @RequestParam Long paidByUserId,
                                 @RequestParam String description,
                                 @RequestParam Double amount) {
        return expenseService.addExpense(groupId, paidByUserId, description, amount);
    }

    //Get expenses for a group
    @GetMapping("/group/{groupId}")
    public List<ExpenseDTO> getExpensesByGroup(@PathVariable Long groupId) {
        return expenseService.getExpensesByGroup(groupId);
    }

    //Get shares of a user
    @GetMapping("/user/{userId}/shares")
    public List<ExpenseShareDTO> getSharesByUser(@PathVariable Long userId) {
        return expenseService.getSharesByUser(userId);
    }
}
