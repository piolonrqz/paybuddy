package com.splitwise.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpenseShareDTO {
    private Long expenseId;
    private String userEmail;
    private Double shareAmount;
}
