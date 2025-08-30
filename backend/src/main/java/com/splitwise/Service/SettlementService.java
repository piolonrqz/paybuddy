package com.splitwise.Service;

import org.springframework.stereotype.Service;

import com.splitwise.Entities.Settlement;
import com.splitwise.Repository.SettlementRepository;
import com.splitwise.Repository.UserRepository;
import com.splitwise.Entities.User;

import java.util.List;

@Service
public class SettlementService {
    private final SettlementRepository settlementRepository;
    private final UserRepository userRepository;

    public SettlementService(SettlementRepository settlementRepository,
                             UserRepository userRepository) {
        this.settlementRepository = settlementRepository;
        this.userRepository = userRepository;
    }

    // ✅ Record a settlement (user1 pays user2)
    public Settlement addSettlement(Long fromUserId, Long toUserId, Double amount) {
        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Settlement settlement = new Settlement();
        settlement.setFromUser(fromUser);
        settlement.setToUser(toUser);
        settlement.setAmount(amount);

        return settlementRepository.save(settlement);
    }

    //Get settlements made by a user
    public List<Settlement> getSettlementsByFromUser(Long userId) {
        return settlementRepository.findPaidById(userId);
    }

    //Get settlements received by a user
    public List<Settlement> getSettlementsByToUser(Long userId) {
        return settlementRepository.findPaidToId(userId);
    }
}
