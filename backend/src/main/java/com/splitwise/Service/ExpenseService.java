package com.splitwise.Service;

import com.splitwise.DTO.ExpenseDTO;
import com.splitwise.DTO.ExpenseShareDTO;
import com.splitwise.Entities.*;
import com.splitwise.Repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseShareRepository expenseShareRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    public ExpenseService(ExpenseRepository expenseRepository, ExpenseShareRepository expenseShareRepository,
                          GroupRepository groupRepository, UserRepository userRepository,
                          GroupMemberRepository groupMemberRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseShareRepository = expenseShareRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    //Add expense and split equally
    public ExpenseDTO addExpense(Long groupId, Long paidByUserId, String description, Double amount) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User paidBy = userRepository.findById(paidByUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = new Expense();
        expense.setGroup(group);
        expense.setPaidBy(paidBy);
        expense.setDescription(description);
        expense.setAmount(amount);

        Expense savedExpense = expenseRepository.save(expense);

        List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
        if (members.isEmpty()) throw new RuntimeException("No members in group");

        double share = amount / members.size();

        for (GroupMember member : members) {
            ExpenseShare shareEntity = new ExpenseShare();
            shareEntity.setExpense(savedExpense);
            shareEntity.setUser(member.getUser());
            shareEntity.setShareAmount(share);
            expenseShareRepository.save(shareEntity);
        }

        return new ExpenseDTO(savedExpense.getId(), description, amount, paidBy.getEmail(), groupId);
    }

    //List expenses for a group
    public List<ExpenseDTO> getExpensesByGroup(Long groupId) {
        List<Expense> expenses = expenseRepository.findByGroupById(groupId);
        List<ExpenseDTO> result = new ArrayList<>();
        for (Expense e : expenses) {
            result.add(new ExpenseDTO(e.getId(), e.getDescription(), e.getAmount(),
                    e.getPaidBy().getEmail(), e.getGroup().getId()));
        }
        return result;
    }

    //Get all shares of a user
    public List<ExpenseShareDTO> getSharesByUser(Long userId) {
        List<ExpenseShare> shares = expenseShareRepository.findByUserId(userId);
        List<ExpenseShareDTO> result = new ArrayList<>();
        for (ExpenseShare s : shares) {
            result.add(new ExpenseShareDTO(
                    s.getExpense().getId(),
                    s.getUser().getEmail(),
                    s.getShareAmount()
            ));
        }
        return result;
    }
}
