package com.splitwise.Service;

import com.splitwise.DTO.SettlementDTO;
import com.splitwise.Entities.Settlement;
import com.splitwise.Entities.User;
import com.splitwise.Repository.SettlementRepository;
import com.splitwise.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final UserRepository userRepository;

    public SettlementService(SettlementRepository settlementRepository, UserRepository userRepository) {
        this.settlementRepository = settlementRepository;
        this.userRepository = userRepository;
    }

    // 🔄 Entity -> DTO
    private SettlementDTO convertToDTO(Settlement settlement) {
        SettlementDTO dto = new SettlementDTO();
        dto.setId(settlement.getId());
        dto.setFromUserId(settlement.getFromUser().getId());
        dto.setFromUserName(settlement.getFromUser().getFirstName() + " " + settlement.getFromUser().getLastName());
        dto.setToUserId(settlement.getToUser().getId());
        dto.setToUserName(settlement.getToUser().getFirstName() + " " + settlement.getToUser().getLastName());
        dto.setAmount(settlement.getAmount());
        return dto;
    }

    // ➕ Add settlement
    public SettlementDTO addSettlement(Long fromUserId, Long toUserId, Double amount) {
        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new RuntimeException("FromUser not found"));

        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new RuntimeException("ToUser not found"));

        Settlement settlement = new Settlement();
        settlement.setFromUser(fromUser);
        settlement.setToUser(toUser);
        settlement.setAmount(amount);

        Settlement saved = settlementRepository.save(settlement);
        return convertToDTO(saved);
    }

    // 📤 Get settlements made by a user
    public List<SettlementDTO> getSettlementsByFromUser(Long userId) {
        return settlementRepository.findPaidById(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 📥 Get settlements received by a user
    public List<SettlementDTO> getSettlementsByToUser(Long userId) {
        return settlementRepository.findPaidToId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 📊 Get ALL settlements related to a user (both paid & received)
    public List<SettlementDTO> getAllSettlementsByUser(Long userId) {
        List<Settlement> settlements = settlementRepository.findPaidById(userId);
        settlements.addAll(settlementRepository.findPaidToId(userId));
        return settlements.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
