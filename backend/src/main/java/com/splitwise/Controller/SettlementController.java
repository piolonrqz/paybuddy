package com.splitwise.Controller;

import com.splitwise.Entities.Settlement;
import com.splitwise.Service.SettlementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    //Record a new settlement (repayment)
    @PostMapping("/add")
    public Settlement addSettlement(@RequestParam Long fromUserId,
                                    @RequestParam Long toUserId,
                                    @RequestParam Double amount) {
        return settlementService.addSettlement(fromUserId, toUserId, amount);
    }

    //Get all settlements made by a user
    @GetMapping("/from/{userId}")
    public List<Settlement> getSettlementsFromUser(@PathVariable Long userId) {
        return settlementService.getSettlementsByFromUser(userId);
    }

    // Get all settlements received by a user
    @GetMapping("/to/{userId}")
    public List<Settlement> getSettlementsToUser(@PathVariable Long userId) {
        return settlementService.getSettlementsByToUser(userId);
    }
}
