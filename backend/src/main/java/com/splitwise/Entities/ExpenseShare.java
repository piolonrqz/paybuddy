package com.splitwise.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "expense_share")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class ExpenseShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private Double shareAmount;

    @ManyToOne
    @JoinColumn(name ="expense_id", nullable = false)
    private Expense expense;

    @ManyToOne
    @JoinColumn(name= "user_id", nullable = false)
    private User user;
}
