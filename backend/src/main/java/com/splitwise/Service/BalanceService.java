package com.splitwise.Service;

import com.splitwise.DTO.BalanceDTO;
import com.splitwise.DTO.SimplifiedDebtDTO;
import com.splitwise.Entities.ExpenseShare;
import com.splitwise.Entities.Settlement;
import com.splitwise.Entities.User;
import com.splitwise.Repository.ExpenseShareRepository;
import com.splitwise.Repository.SettlementRepository;
import com.splitwise.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BalanceService {

    private final ExpenseShareRepository expenseShareRepository;
    private final SettlementRepository settlementRepository;
    private final UserRepository userRepository;

    public BalanceService(ExpenseShareRepository expenseShareRepository,
                          SettlementRepository settlementRepository,
                          UserRepository userRepository) {
        this.expenseShareRepository = expenseShareRepository;
        this.settlementRepository = settlementRepository;
        this.userRepository = userRepository;
    }

    //Calculate balances for all users
    public List<BalanceDTO> calculateBalances() {
        Map<Long, Double> userBalances = new HashMap<>();

        // Step 1: Expenses
        List<ExpenseShare> allShares = expenseShareRepository.findAll();
        for (ExpenseShare share : allShares) {
            Long userId = share.getUser().getId();
            userBalances.put(userId, userBalances.getOrDefault(userId, 0.0) - share.getShareAmount());

            Long paidById = share.getExpense().getPaidBy().getId();
            userBalances.put(paidById, userBalances.getOrDefault(paidById, 0.0) + share.getShareAmount());
        }

        // Step 2: Settlements
        List<Settlement> allSettlements = settlementRepository.findAll();
        for (Settlement settlement : allSettlements) {
            Long fromUserId = settlement.getFromUser().getId();
            Long toUserId = settlement.getToUser().getId();
            Double amount = settlement.getAmount();

            userBalances.put(fromUserId, userBalances.getOrDefault(fromUserId, 0.0) + amount);
            userBalances.put(toUserId, userBalances.getOrDefault(toUserId, 0.0) - amount);
        }

        // Step 3: Convert to DTO
        List<BalanceDTO> result = new ArrayList<>();
        for (Map.Entry<Long, Double> entry : userBalances.entrySet()) {
            User user = userRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            result.add(new BalanceDTO(user.getEmail(), entry.getValue()));
        }

        return result;
    }

    //Get balance of a single user
    public BalanceDTO getUserBalance(Long userId) {
        List<BalanceDTO> balances = calculateBalances();
        String email = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getEmail();

        return balances.stream()
                .filter(b -> b.getUserEmail().equals(email))
                .findFirst()
                .orElse(new BalanceDTO(email, 0.0));
    }

    //Simplify debts to minimal transactions
    public List<SimplifiedDebtDTO> simplifyDebts() {
        Map<String, Double> balances = new HashMap<>();
        for (BalanceDTO b : calculateBalances()) {
            balances.put(b.getUserEmail(), b.getBalance());
        }

        PriorityQueue<Map.Entry<String, Double>> creditors = new PriorityQueue<>(
                (a, b) -> Double.compare(b.getValue(), a.getValue())
        );
        PriorityQueue<Map.Entry<String, Double>> debtors = new PriorityQueue<>(
                (a, b) -> Double.compare(a.getValue(), b.getValue())
        );

        for (Map.Entry<String, Double> entry : balances.entrySet()) {
            if (entry.getValue() > 0) creditors.add(entry);
            else if (entry.getValue() < 0) debtors.add(entry);
        }

        List<SimplifiedDebtDTO> transactions = new ArrayList<>();

        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            Map.Entry<String, Double> creditor = creditors.poll();
            Map.Entry<String, Double> debtor = debtors.poll();

            double settleAmount = Math.min(creditor.getValue(), -debtor.getValue());

            transactions.add(new SimplifiedDebtDTO(debtor.getKey(), creditor.getKey(), settleAmount));

            creditor.setValue(creditor.getValue() - settleAmount);
            debtor.setValue(debtor.getValue() + settleAmount);

            if (creditor.getValue() > 0) creditors.add(creditor);
            if (debtor.getValue() < 0) debtors.add(debtor);
        }

        return transactions;
    }
}
