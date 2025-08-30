package com.splitwise.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "settlements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Who is paying
    @ManyToOne
    @JoinColumn(name = "from_user_id", nullable = false)
    private User fromUser;

    // Who is receiving
    @ManyToOne
    @JoinColumn(name = "to_user_id", nullable = false)
    private User toUser;

    //How much money
    private Double amount;
}
