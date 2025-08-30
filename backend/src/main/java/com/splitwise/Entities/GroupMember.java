package com.splitwise.Entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="group_members")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="group_id", nullable = false)
    private Group group;
}
