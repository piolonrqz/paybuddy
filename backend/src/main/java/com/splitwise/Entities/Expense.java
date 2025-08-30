package com.splitwise.Entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;

    private String description;
    private double amount;


    @ManyToOne
    @JoinColumn(name= "group_id", nullable = false)
    private Group group;


    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User paidBy;

    private LocalDateTime createdAt = LocalDateTime.now();
}
