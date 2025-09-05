package com.splitwise.Controller;

import com.splitwise.DTO.SettlementDTO;
import com.splitwise.Service.SettlementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    // ➕ Add a new settlement
    @PostMapping
    public ResponseEntity<SettlementDTO> addSettlement(
            @RequestParam Long fromUserId,
            @RequestParam Long toUserId,
            @RequestParam Double amount) {
        SettlementDTO settlement = settlementService.addSettlement(fromUserId, toUserId, amount);
        return ResponseEntity.ok(settlement);
    }

    // 📤 Get settlements made by a user
    @GetMapping("/from/{userId}")
    public ResponseEntity<List<SettlementDTO>> getSettlementsByFromUser(@PathVariable Long userId) {
        return ResponseEntity.ok(settlementService.getSettlementsByFromUser(userId));
    }

    // 📥 Get settlements received by a user
    @GetMapping("/to/{userId}")
    public ResponseEntity<List<SettlementDTO>> getSettlementsByToUser(@PathVariable Long userId) {
        return ResponseEntity.ok(settlementService.getSettlementsByToUser(userId));
    }

    // 📊 Get all settlements related to a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SettlementDTO>> getAllSettlementsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(settlementService.getAllSettlementsByUser(userId));
    }
}
