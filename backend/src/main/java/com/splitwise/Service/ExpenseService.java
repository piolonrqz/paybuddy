package com.splitwise.Service;
import org.springframework.stereotype.Service;

import com.splitwise.Entities.Expense;
import com.splitwise.Entities.ExpenseShare;
import com.splitwise.Entities.GroupMember;
import com.splitwise.Repository.ExpenseRepository;
import com.splitwise.Repository.ExpenseShareRepository;
import com.splitwise.Repository.GroupMemberRepository;
import com.splitwise.Repository.GroupRepository;
import com.splitwise.Repository.UserRepository;
import com.splitwise.Entities.*;
import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseShareRepository expenseShareRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    public ExpenseService(ExpenseRepository expenseRepository, ExpenseShareRepository expenseShareRepository, GroupRepository groupRepository, UserRepository userRepository, GroupMemberRepository groupMemberRepository){
        this.expenseRepository = expenseRepository;
        this.expenseShareRepository = expenseShareRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    //add expense and auto-split equally among group members
    public Expense addExpense(Long groupId, Long paidByUserId, String description, Double amount) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User paidBy = userRepository.findById(paidByUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Save expense
        Expense expense = new Expense();
        expense.setGroup(group);
        expense.setPaidBy(paidBy);
        expense.setDescription(description);
        expense.setAmount(amount);

        Expense savedExpense = expenseRepository.save(expense);

        // Get members of group
        List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);

        if (members.isEmpty()) {
            throw new RuntimeException("No members in group");
        }

        // Calculate equal share
        double share = amount / members.size();

        for (GroupMember member : members) {
            ExpenseShare shareEntity = new ExpenseShare();
            shareEntity.setExpense(savedExpense);
            shareEntity.setUser(member.getUser());
            shareEntity.setShareAmount(share);
            expenseShareRepository.save(shareEntity);
        }

        return savedExpense;
    }

    //List expenses for a group
    public List<Expense> getExpensesByGroup(Long groupId) {
        return expenseRepository.findByGroupById(groupId);
    }

    //Get all shares of a user
    public List<ExpenseShare> getSharesByUser(Long userId) {
        return expenseShareRepository.findByUserId(userId);
    }
}
